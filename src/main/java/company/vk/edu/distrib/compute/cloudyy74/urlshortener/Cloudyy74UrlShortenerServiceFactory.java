package company.vk.edu.distrib.compute.cloudyy74.urlshortener;

import company.vk.edu.distrib.compute.AbstractHttpServiceFactory;

import java.io.IOException;

public class Cloudyy74UrlShortenerServiceFactory extends AbstractHttpServiceFactory<Cloudyy74UrlShortenerService> {
    @Override
    protected Cloudyy74UrlShortenerService doCreate(int port) throws IOException {
        return new Cloudyy74UrlShortenerService(port);
    }
}
