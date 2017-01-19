package io.opentracing.play

import io.opentracing.Span
import io.opentracing.tag.Tags
import play.api.mvc.{RequestHeader, Result}

/**
 * Applies standard tags
 * @see https://raw.githubusercontent.com/opentracing/specification/1.0/data_conventions.yaml
 */
class StandardSpanTagger extends SpanTagger {
  def tag(span: Span, request: RequestHeader, result: Result) = {
    Tags.HTTP_STATUS.set(span, result.header.status)
    Tags.HTTP_METHOD.set(span, request.method)
    Tags.HTTP_URL.set(span, s"${if (request.secure) "http" else "https"}://${request.host}/${request.uri}")
    Tags.PEER_HOSTNAME.set(span, request.domain)
    Tags.PEER_PORT.set(span, request.host.split(":").lift(1).fold(if (request.secure) 443.toShort else 80.toShort)(_.toShort))
  }
}
