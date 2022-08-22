package vertx;

import io.vertx.core.http.HttpServerResponse;
import io.vertx.ext.web.RoutingContext;

public class Routes {
    void rootHandler(RoutingContext routingContext) {
        final HttpServerResponse response = routingContext.response();
        response.setChunked(true);
        response.write("Hello chatty");
        response.end();
    }
}
