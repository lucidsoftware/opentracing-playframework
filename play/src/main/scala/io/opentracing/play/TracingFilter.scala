package io.opentracing.play

import io.opentracing.Tracer

class TracingFilter(name: String, protected[this] val tracer: Tracer, taggers: Traversable[SpanTagger]) extends BaseTracingFilter(name, taggers)
