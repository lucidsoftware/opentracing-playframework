package io.opentracing.play.active

import io.opentracing.contrib.global.GlobalTracer
import io.opentracing.contrib.spanmanager.DefaultSpanManager
import io.opentracing.play.{BaseTracingFilter, SpanTagger}
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

class ActiveTracingFilter(name: String, taggers: Traversable[SpanTagger]) extends BaseTracingFilter(name, taggers) {
  protected[this] def tracer = GlobalTracer.get

  override def apply(next: EssentialAction) = EssentialAction { request =>
    val managedSpan = DefaultSpanManager.getInstance.manage(spanBuilder(request).start())
    next(request).map { result =>
      taggers.foreach(_.tag(managedSpan.getSpan, request, result))
      managedSpan.getSpan.finish()
      managedSpan.release()
      result
    }
  }
}
