package io.opentracing.play

import io.opentracing.Span
import play.api.mvc.{RequestHeader, Result}

/**
 * Applies tags from [[RequestHeader#tags]]
 */
class TagsSpanTagger extends SpanTagger {
  def tag(span: Span, request: RequestHeader, result: Result) = {
    request.tags.foreach { case (key, value) => span.setTag(s"play.$key", value) }
  }
}

object TagsSpanTagger extends TagsSpanTagger
