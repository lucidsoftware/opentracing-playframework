package io.opentracing.play

import io.opentracing.Tracer
import io.opentracing.propagation.Format
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

abstract class BaseTracingFilter(name: String, taggers: Traversable[SpanTagger]) extends EssentialFilter {

  protected[this] def tracer: Tracer

  protected[this] def spanBuilder(request: RequestHeader) = {
    val parent = tracer.extract(Format.Builtin.HTTP_HEADERS, new HeadersTextMap(request.headers))
    tracer.buildSpan(Routes.controllerName(request).getOrElse(name)).asChildOf(parent)

  }

  def apply(next: EssentialAction) = EssentialAction { request =>
    val span = spanBuilder(request).start()
    val result = next(request)
    result.map { result =>
      val finish = (System.nanoTime / 1000).toInt
      taggers.foreach(_.tag(span, request, result))
      span.finish(finish)
      result
    }
  }
}
