package company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers;

import com.sun.net.httpserver.HttpExchange;

public class Request {
    private final String path;

    private Request(Builder builder) {
        path = builder.path;
    }

    public String path() {
        return path;
    }

    public static Request from(HttpExchange exchange) {
        return new Builder()
                .setPath(exchange.getRequestURI().getPath())
                .build();
    }

    public static final class Builder {
        private Builder() {}

        private String path;

        public Builder setPath(String path) {
            this.path = path;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }
}
