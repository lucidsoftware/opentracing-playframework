package io.opentracing.play

import play.api.mvc.RequestHeader
import play.api.routing.Router

object Routes {

  def controllerName(request: RequestHeader) = request.tags.get(Router.Tags.RouteController).map { controller =>
    request.tags.get(Router.Tags.RouteActionMethod).fold(controller) { method => s"$controller.$method" }
  }

}
