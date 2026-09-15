package company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public abstract class BaseController implements HttpHandler {
    private final Map<String, Function<Request, Response>> methods = Map.of(
            "GET", this::GET,
            "POST", this::POST,
            "PUT", this::PUT,
            "DELETE", this::DELETE
    );

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        var request = Request.from(exchange);

        var method = methods.get(exchange.getRequestMethod());
        if (method == null) {
            throw new IOException("invalid method");
        }

        var response = method.apply(request);
        if (response == null) {
            response = Response.builder().build();
        }

        makeExchange(exchange, response);
        exchange.close();
    }

    private void makeExchange(HttpExchange exchange, Response response) throws IOException {
        exchange.sendResponseHeaders(response.status(), response.length());
    }

    public abstract String path();

    public abstract Response GET(Request request);

    public abstract Response POST(Request request);

    public abstract Response PUT(Request request);

    public abstract Response DELETE(Request request);
}
