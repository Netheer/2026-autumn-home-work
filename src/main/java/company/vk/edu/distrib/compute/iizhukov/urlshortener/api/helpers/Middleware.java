package company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers;

import java.util.function.Function;

@FunctionalInterface
public interface Middleware {
    public Response handle(Request request, Handler handler);
}
