package io.opentracing.play

import io.opentracing.Tracer
import io.opentracing.propagation.Format
import io.opentracing.tag.Tags
import io.opentracing.threadcontext.ThreadContextSpan
import java.util.function.Supplier
import play.api.mvc._
import scala.concurrent.ExecutionContext
import scala.util.Try

class TracingFilter(protected[this] val tracer: Tracer, taggers: Traversable[SpanTagger])(implicit ec: ExecutionContext) extends EssentialFilter {

  protected[this] def spanBuilder(request: RequestHeader) = {

  }

  private def toSupplier[A](a: => A) = new Supplier[A] { def get() = a }

  def apply(next: EssentialAction) = EssentialAction { request =>
    val builder = tracer.buildSpan(Routes.endpointName(request).getOrElse(request.method))
      .withTag(Tags.SPAN_KIND.getKey, Tags.SPAN_KIND_SERVER)
    Try(tracer.extract(Format.Builtin.HTTP_HEADERS, new HeadersTextMap(request.headers))).foreach(builder.asChildOf)
    val span = builder.start()
    ThreadContextSpan.withSpan(span, toSupplier {
      next(request).map { result =>
        taggers.foreach(_.tag(span, request, result))
        result
      }
    })
  }

}
