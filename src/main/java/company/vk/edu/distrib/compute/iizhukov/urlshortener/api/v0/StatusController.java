package company.vk.edu.distrib.compute.iizhukov.urlshortener.api.v0;

import company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers.BaseController;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers.HttpStatus;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers.Request;
import company.vk.edu.distrib.compute.iizhukov.urlshortener.api.helpers.Response;

public class StatusController extends BaseController {
    @Override
    public String path() {
        return "/v0/status";
    }

    @Override
    public Response GET(Request request) {
        return Response.builder()
                .setStatus(HttpStatus.OK)
                .build();
    }

    @Override
    public Response POST(Request request) {
        return null;
    }

    @Override
    public Response PUT(Request request) {
        return null;
    }

    @Override
    public Response DELETE(Request request) {
        return null;
    }
}
