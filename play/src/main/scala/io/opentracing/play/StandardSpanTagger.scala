package io.opentracing.play

import com.google.common.net.InetAddresses
import io.opentracing.Span
import io.opentracing.tag.Tags
import java.net.{Inet4Address, Inet6Address}
import java.nio.ByteBuffer
import play.api.mvc.{RequestHeader, Result}
import scala.util.Try

/**
 * Applies standard tags
 * @see https://raw.githubusercontent.com/opentracing/specification/1.0/data_conventions.yaml
 */
class StandardSpanTagger extends SpanTagger {
  def tag(span: Span, request: RequestHeader, result: Option[Result]) = {
    Tags.HTTP_METHOD.set(span, request.method)
    result.foreach { result =>
      Tags.HTTP_STATUS.set(span, result.header.status)
    }
    Tags.HTTP_URL.set(span, request.uri)
    request.headers
      .get("X-Forwarded-Port")
      .flatMap(port => Try(port.toInt.toShort).toOption)
      .foreach(Tags.PEER_PORT.set(span, _))
    Try(InetAddresses.forString(request.remoteAddress)).foreach {
      case ip: Inet4Address =>
        Tags.PEER_HOST_IPV4.set(span, ByteBuffer.wrap(ip.getAddress).getInt)
      case ip: Inet6Address =>
        Tags.PEER_HOST_IPV6.set(span, ip.getHostAddress.takeWhile(_ != '%'))
    }

  }
}
