package company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers;

public class Response {
    private final int status;
    private final String content;
    private final int length;

    private Response(Builder builder) {
        status = builder.status.code();
        content = builder.content;
        length = builder.content.length();
    }

    public int status() {
        return status;
    }

    public String content() {
        return content;
    }

    public int length() {
        return length;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private Builder() {}

        private HttpStatus status = HttpStatus.SERVER_ERROR;
        private String content = "";

        public Builder setStatus(HttpStatus status) {
            this.status = status;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public Response build() {
            return new Response(this);
        }
    }
}
