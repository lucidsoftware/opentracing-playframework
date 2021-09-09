package io.opentracing.play

import io.opentracing.Span
import play.api.libs.typedmap.TypedKey
import play.api.mvc.{RequestHeader, Result}

/**
 * Applies attributes from [[https://www.playframework.com/documentation/2.8.x/api/scala/play/api/mvc/RequestHeader.html RequestHeader#attrs]].
 *
 * @param attributeKeys The set of attributes to read from the [[https://www.playframework.com/documentation/2.8.x/api/scala/play/api/mvc/RequestHeader.html  RequestHeader]]. Must be explicitly stated because
 *  [[https://www.playframework.com/documentation/2.8.x/api/scala/play/api/mvc/RequestHeader.html RequestHeader#attrs]] does not support iterating over all attributes on a request.
 *
 *  Attributes may or may not have a displayName. In order to ensure that the tags set on the Span are sensible, only
 *  Attributes with a displayName set will be used to tag the Span.
 */
class RequestAttributesSpanTagger(attributeKeys: Set[TypedKey[String]]) extends SpanTagger {

  def tag(span: Span, request: RequestHeader, result: Option[Result]): Unit =
    attributeKeys.foreach { key =>
      key.displayName.foreach { name =>
        request.attrs
          .get(key)
          .foreach(value => span.setTag(s"play.$name", value))
      }
    }
}
