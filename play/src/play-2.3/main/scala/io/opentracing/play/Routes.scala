package io.opentracing.play

import play.api.mvc.RequestHeader
import play.api.{Routes => PlayRoutes}

object Routes {

  def controllerName(request: RequestHeader) = request.tags.get(PlayRoutes.ROUTE_CONTROLLER).map { controller =>
    request.tags.get(PlayRoutes.ROUTE_ACTION_METHOD).fold(controller) { method => s"$controller.$method" }
  }

}
