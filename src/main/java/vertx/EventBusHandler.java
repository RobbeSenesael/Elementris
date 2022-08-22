package vertx;

import io.vertx.core.Vertx;
import io.vertx.ext.bridge.PermittedOptions;
import io.vertx.ext.web.handler.sockjs.BridgeOptions;
import io.vertx.ext.web.handler.sockjs.SockJSHandler;

public class EventBusHandler {
    private final SockJSHandler sockJSHandler;

    EventBusHandler(final Vertx vertx) {
        sockJSHandler = SockJSHandler.create(vertx);
    }

    private void addBridgeOptions() {
        final PermittedOptions inbound = new PermittedOptions().setAddressRegex("tetris-27\\.socket\\..+");
        final PermittedOptions outbound = inbound;
        final BridgeOptions options = new BridgeOptions().addInboundPermitted(inbound).addOutboundPermitted(outbound);
        sockJSHandler.bridge(options);
    }

    SockJSHandler create() {
        addBridgeOptions();
        return sockJSHandler;
    }
}
