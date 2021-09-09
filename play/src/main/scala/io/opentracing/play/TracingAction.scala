package io.opentracing.play

import io.opentracing.{Span, Tracer}
import io.opentracing.util.GlobalTracer
import io.opentracing.propagation.Format
import io.opentracing.tag.Tags
import io.opentracing.threadcontext.ContextSpan
import java.util.concurrent.Callable

import akka.stream.Materializer
import play.api.mvc._

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success, Try}

private object RequestSpan {
  def apply(tracer: Tracer, request: RequestHeader): Span =
    tracer
      .buildSpan(Routes.endpointName(request).getOrElse(s"HTTP ${request.method}"))
      .asChildOf(tracer.extract(Format.Builtin.HTTP_HEADERS, new HeadersTextMap(request.headers)))
      .withTag(Tags.SPAN_KIND.getKey, Tags.SPAN_KIND_SERVER)
      .start()
}
class TracingRequest[+A](val span: Span, request: Request[A]) extends WrappedRequest(request)

/**
 * A class to make it easy to create Actions that take a TracingRequest and
 * run with a Span stored in thread local storage.
 *
 * Normally you would create an object that extends this class, and use that to create Actions.
 *
 * {{{
 * object TracingAction extends TracingActionBuilder(ContextSpan.DEFAULT, Nil)
 * }}}
 */
class TracingActionBuilder(
  protected[this] val tracer: Tracer,
  protected[this] val contextSpan: ContextSpan,
  taggers: Traversable[SpanTagger]
)(val parser: BodyParser[AnyContent])(implicit ec: ExecutionContext)
    extends ActionBuilder[TracingRequest, AnyContent] {

  /**
   * Finish tagging the span and finish it. A subclass may wish to
   */
  protected def finishSpan[A](request: TracingRequest[A], result: Option[Result]) = {
    taggers.foreach(_.tag(request.span, request, result))
    request.span.finish()
  }

  final def invokeBlock[A](request: Request[A], block: TracingRequest[A] => Future[Result]): Future[Result] = {
    val span = RequestSpan(tracer, request)
    val tracingRequest = new TracingRequest(span, request)
    contextSpan
      .set(span)
      .call(new Callable[Future[Result]] {

        private def failureHandler(exception: Throwable): Future[Result] = {
          Tags.ERROR.set(tracingRequest.span, true)
          finishSpan(tracingRequest, None)
          Future.failed(exception)
        }

        def call() =
          Try(block(tracingRequest)) match {
            case Failure(exception) =>
              failureHandler(exception)
            case Success(resultFuture) =>
              resultFuture
                .map { result =>
                  finishSpan(tracingRequest, Some(result))
                  result
                }
                .recoverWith {
                  case exception =>
                    failureHandler(exception)
                }
          }

      })
  }

  override protected def executionContext: ExecutionContext = ec
}

/**
 * Like TracingRequest but uses ContextSpan.DEFAULT and GlobalTracer for the context and tracer.
 */
class DefaultTracingActionBuilder(taggers: Traversable[SpanTagger])(
  implicit ec: ExecutionContext,
  mat: Materializer
) extends TracingActionBuilder(GlobalTracer.get, ContextSpan.DEFAULT, taggers)(new BodyParsers.Default)
