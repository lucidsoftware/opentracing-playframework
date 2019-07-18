package io.opentracing.play

import io.opentracing.Span
import play.api.mvc.{RequestHeader, Result}

/**
 * Applies tags from [[RequestHeader#tags]]
 */
@deprecated("Tags are deprecated in Play 2.6; use RequestAttributesSpanTagger instead", "0.14")
class TagsSpanTagger(include: String => Boolean) extends SpanTagger {
  def tag(span: Span, request: RequestHeader, result: Result) = {
    request.tags.foreach { case (key, value) => if (include(key)) span.setTag(s"play.$key", value) }
  }
}
