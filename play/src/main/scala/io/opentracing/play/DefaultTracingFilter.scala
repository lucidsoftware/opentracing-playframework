package io.opentracing.play

import io.opentracing.contrib.global.GlobalTracer
import io.opentracing.threadcontext.ContextSpan
import scala.concurrent.ExecutionContext

@deprecated("Filters don't work well for setting thread local storage, use DefaultTracingAction instead", "0.12")
class DefaultTracingFilter(taggers: Traversable[SpanTagger])(implicit executionContext: ExecutionContext) extends TracingFilter(ContextSpan.DEFAULT, taggers) {
  protected[this] def tracer = GlobalTracer.get
}
