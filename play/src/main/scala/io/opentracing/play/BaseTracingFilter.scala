package io.opentracing.play

import io.opentracing.Tracer
import io.opentracing.propagation.Format
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._
import scala.util.control.NonFatal

abstract class BaseTracingFilter(taggers: Traversable[SpanTagger]) extends EssentialFilter {

  protected[this] def tracer: Tracer

  protected[this] def spanBuilder(request: RequestHeader) = {
    val builder = tracer.buildSpan(Routes.endpointName(request).getOrElse(request.method))
    try {
      builder.asChildOf(tracer.extract(Format.Builtin.HTTP_HEADERS, new HeadersTextMap(request.headers)))
    } catch {
      case NonFatal(e) => builder
    }
  }

  def apply(next: EssentialAction) = EssentialAction { request =>
    val span = spanBuilder(request).start()
    val result = next(request)
    result.map { result =>
      taggers.foreach(_.tag(span, request, result))
      span.finish()
      result
    }
  }
}
