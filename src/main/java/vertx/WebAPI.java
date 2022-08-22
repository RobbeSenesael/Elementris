package vertx;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;

public class WebAPI extends AbstractVerticle {
    @Override
    public void start() {
        final HttpServer server = vertx.createHttpServer();
        final Router router = Router.router(vertx);
        final Routes routes = new Routes();
        router.route("/").handler(routes::rootHandler);
        router.route("/static/*").handler(StaticHandler.create());
        router.route("/tetris-27/socket/*").handler(new EventBusHandler(vertx).create());
        server.requestHandler(router::accept).listen(config().getInteger("http.port", 8027));
    }
}
