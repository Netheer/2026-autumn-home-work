package company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers;

import java.io.IOException;

@FunctionalInterface
public interface Handler {
    Response handle(Request request);
}
