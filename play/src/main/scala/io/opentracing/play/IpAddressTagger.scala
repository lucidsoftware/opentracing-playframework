package io.opentracing.play

import io.opentracing.Span
import io.opentracing.tag.Tags
import java.net.{Inet4Address, Inet6Address, NetworkInterface}
import java.nio.ByteBuffer
import play.api.mvc.{RequestHeader, Result}
import scala.collection.JavaConversions._

class IpAddressTagger extends SpanTagger {

  private[this] val addresses = NetworkInterface.getNetworkInterfaces
    .filter(interface => interface.isUp && !interface.isVirtual && !interface.isLoopback)
    .flatMap(_.getInetAddresses)

  def tag(span: Span, request: RequestHeader, result: Result) = addresses.foreach {
    case address: Inet4Address => Tags.PEER_HOST_IPV4.set(span, ByteBuffer.wrap(address.getAddress).getInt)
    case address: Inet6Address => Tags.PEER_HOST_IPV6.set(span, address.getHostAddress.split('%').head)
    case _ =>
  }

}
