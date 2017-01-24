package io.opentracing.play

import io.opentracing.Span
import play.api.mvc.{Headers, RequestHeader, Result}
import play.mvc.Http.HeaderNames
import scala.util.control.Exception.catching

class ContentTagger extends SpanTagger {
  import ContentTagger._

  def tag(span: Span, request: RequestHeader, result: Result) = {
    result.header.headers.get(HeaderNames.CONTENT_LENGTH)
      .flatMap(length => catching(classOf[NumberFormatException]).opt(length.toLong))
      .foreach(span.setTag(contentLengthTag, _))
    result.header.headers.get(HeaderNames.CONTENT_TYPE).foreach(span.setTag(contentTypeTag, _))
  }
}

object ContentTagger {
  val contentLengthTag = "http.contentLength"
  val contentTypeTag = "http.contentType"
}
