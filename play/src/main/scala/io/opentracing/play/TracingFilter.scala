package io.opentracing.play

import io.opentracing.Tracer
import io.opentracing.propagation.Format
import io.opentracing.tag.Tags
import io.opentracing.threadcontext.ContextSpan
import java.util.function.Supplier
import play.api.mvc._
import scala.concurrent.ExecutionContext

@deprecated("Filters don't work well for setting thread local storage, use TracingAction instead", "0.12")
abstract class TracingFilter(protected[this] val contextSpan: ContextSpan, taggers: Traversable[SpanTagger])(implicit ec: ExecutionContext) extends EssentialFilter {

  private[this] def toSupplier[A](a: => A) = new Supplier[A] { def get() = a }

  protected[this] def tracer: Tracer


  def apply(next: EssentialAction) = EssentialAction { request =>
    val span = tracer.buildSpan(Routes.endpointName(request).getOrElse(s"HTTP ${request.method}"))
      .asChildOf(tracer.extract(Format.Builtin.HTTP_HEADERS, new HeadersTextMap(request.headers)))
      .withTag(Tags.SPAN_KIND.getKey, Tags.SPAN_KIND_SERVER)
      .start()
    contextSpan.set(span).supply(toSupplier {
      next(request).map { result =>
        taggers.foreach(_.tag(span, request, result))
        span.finish()
        result
      }
    })
  }

}
