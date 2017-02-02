package io.opentracing.play.active

import io.opentracing.contrib.global.GlobalTracer
import io.opentracing.contrib.spanmanager.DefaultSpanManager
import io.opentracing.play.{BaseTracingFilter, SpanTagger}
import io.opentracing.tag.Tags
import play.api.libs.concurrent.Execution.Implicits.defaultContext
import play.api.mvc._

class ActiveTracingFilter(taggers: Traversable[SpanTagger]) extends BaseTracingFilter(taggers) {
  protected[this] def tracer = GlobalTracer.get

  override def apply(next: EssentialAction) = EssentialAction { request =>
    val oldManagedSpan = DefaultSpanManager.getInstance.currentSpan()
    val span = spanBuilder(request).start()
    val managedSpan = DefaultSpanManager.getInstance.manage(span)
    next(request).map { result =>
      taggers.foreach(_.tag(span, request, result))
      span.finish()
      managedSpan.release()
      DefaultSpanManager.getInstance().manage(oldManagedSpan)
      result
    }
  }
}
