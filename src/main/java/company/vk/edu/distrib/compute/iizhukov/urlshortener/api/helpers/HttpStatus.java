package company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers;

public enum HttpStatus {
    OK(200),
    CREATED(201),

    BAD_REQUEST(400),
    NOT_FOUND(404),

    SERVER_ERROR(500);

    private final int code;

    HttpStatus(int code) {
        this.code = code;
    }

    public int code() {
        return code;
    }
}
