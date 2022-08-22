package run;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import jdbcinteractor.JdbcInteractor;
import vertx.WebAPI;
import vertx.messagehandler.*;

public class Program extends AbstractVerticle {

    public static void main(String... args) {
        final Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new WebAPI());
        vertx.deployVerticle(new GameLoopHandler());
        vertx.deployVerticle(new UserInputHandler());
        vertx.deployVerticle(new VerificationHandler());
        vertx.deployVerticle(new JdbcInteractor());
    }

    @Override
    public void start() {
        config().getJsonObject("Components")
                .forEach(entry -> {
                    final JsonObject json = (JsonObject) entry.getValue();
                    final String optionsKey = "options";
                    if (json.containsKey(optionsKey)) {
                        final JsonObject options = ((JsonObject) entry.getValue()).getJsonObject(optionsKey);
                        vertx.deployVerticle(entry.getKey(), new DeploymentOptions(options));
                    } else {
                        vertx.deployVerticle(entry.getKey());
                    }
                });
    }
}
