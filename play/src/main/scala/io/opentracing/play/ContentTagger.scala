package io.opentracing.play
import io.opentracing.Span
import play.api.mvc.{RequestHeader, Result}

class ContentTagger extends SpanTagger {
  import ContentTagger._

  def tag(span: Span, request: RequestHeader, result: Option[Result]) =
    result.foreach { result =>
      result.body.contentLength.foreach(span.setTag(contentLengthTag, _))
      result.body.contentType.foreach(span.setTag(contentTypeTag, _))
    }
}

object ContentTagger extends ContentTagger {
  val contentLengthTag = "http.response.contentLength"
  val contentTypeTag = "http.response.contentType"
}
