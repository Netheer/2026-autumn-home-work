package company.vk.edu.distrib.compute.iizhukov.urlshortener;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import com.sun.net.httpserver.HttpServer;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers.BaseController;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.api.v0.LinksController;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.api.v0.StatusController;
import company.vk.edu.distrib.compute.urlshortener.UrlShortenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Application implements UrlShortenerService {
    private static final Logger log = LoggerFactory.getLogger(Application.class);

    private final Collection<BaseController> registry =
            List.of(
                    new StatusController(),
                    new LinksController()
            );

    @Nullable
    private HttpServer server;

    public void init(int port) throws IOException {
        server = HttpServer.create(new InetSocketAddress(port), 1);

        registry.forEach(controller -> {
            Objects.requireNonNull(server).createContext(controller.path(), controller);
            log.info("Controller %s was registered".formatted(controller.getClass().getName()));
        });
    }

    @Override
    public void start() {
        if (server == null) {
            throw new IllegalStateException("Application must be initialized");
        }

        server.start();
    }

    @Override
    public void stop() {
        if (server == null) {
            return;
        }

        server.stop(1);
    }
}
