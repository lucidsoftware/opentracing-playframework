package io.opentracing.play

import com.google.common.net.InetAddresses
import io.opentracing.Span
import java.net.{Inet4Address, Inet6Address}
import java.nio.ByteBuffer
import play.api.mvc.{RequestHeader, Result}
import scala.util.{Failure, Success, Try}

/**
 * Applies tags for the remote IP address.
 */
class RemoteSpanTagger extends SpanTagger {
  import RemoteSpanTagger._

  def tag(span: Span, request: RequestHeader, result: Result) = {
    Try(InetAddresses.forString(request.remoteAddress)) match {
      case Success(ip: Inet4Address) => span.setTag(RemoteIp4Tag, ByteBuffer.wrap(ip.getAddress).getInt)
      case Success(ip: Inet6Address) => span.setTag(RemoteIp6Tag, ip.toString)
      case Success(_) | Failure(_: IllegalArgumentException) =>
      case Failure(e) => throw e
    }
  }
}

object RemoteSpanTagger {
  val RemoteIp4Tag = "remote.ipv4"
  val RemoteIp6Tag = "remote.ipv6"
}
