package io.opentracing.play

import io.opentracing.Tracer

class TracingFilter(protected[this] val tracer: Tracer, taggers: Traversable[SpanTagger]) extends BaseTracingFilter(taggers)
