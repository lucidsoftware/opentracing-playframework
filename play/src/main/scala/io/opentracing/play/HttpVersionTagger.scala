package io.opentracing.play
import io.opentracing.Span
import play.api.mvc.{RequestHeader, Result}

class HttpVersionTagger extends SpanTagger {
  import HttpVersionTagger._

  def tag(span: Span, request: RequestHeader, result: Result) = {
    span.setTag(HttpVersionTag, request.version)
  }
}

object HttpVersionTagger extends HttpVersionTagger {
  val HttpVersionTag = "http.version"
}
