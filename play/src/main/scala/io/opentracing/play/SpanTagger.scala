package io.opentracing.play

import io.opentracing.Span
import play.api.mvc.{RequestHeader, Result}

abstract class SpanTagger {
  def tag(span: Span, request: RequestHeader, result: Option[Result])
}
