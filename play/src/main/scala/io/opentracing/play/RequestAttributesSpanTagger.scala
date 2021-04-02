package io.opentracing.play

import io.opentracing.Span
import play.api.libs.typedmap.TypedKey
import play.api.mvc.{RequestHeader, Result}

/**
 * Applies attributes from [[RequestHeader#attrs]].
 *
 * @param attributeKeys The set of attributes to read from the [[RequestHeader]]. Must be explicitly stated because
 *  [[RequestHeader#attrs]] does not support iterating over all attributes on a request.
 *
 *  Attributes may or may not have a displayName. In order to ensure that the tags set on the Span are sensible, only
 *  Attributes with a displayName set will be used to tag the Span.
 */
class RequestAttributesSpanTagger(attributeKeys: Set[TypedKey[String]]) extends SpanTagger {

  def tag(span: Span, request: RequestHeader, result: Option[Result]) = {
    attributeKeys.foreach { key =>
      key.displayName.foreach { name =>
        request.attrs.get(key).foreach(value => span.setTag(s"play.$name", value))
      }
    }
  }
}
