package com.dimordovin.uploadservice.service;

import com.dimordovin.uploadservice.dto.UrlDTO;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.ClientConfig;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;

public class ImageService {

    private Client client;
    public static String IMAGE_SERVICE_BASE_URI = System.getenv("IMAGE_SERVICE_BASE_URL");

    public ImageService() {
        ClientConfig cc = new DefaultClientConfig();
        cc.getClasses().add(JacksonJsonProvider.class);
        client = Client.create(cc);
    }

    public void updateImage(String id, String url) {
        WebResource updateResource =
                client.resource( IMAGE_SERVICE_BASE_URI + "/api/images/" + id);

        UrlDTO dto = new UrlDTO(url);

        ClientResponse response = updateResource.accept("application/json")
                .type("application/json").post(ClientResponse.class, dto);

        if (response.getStatus() != 200) {
            throw new RuntimeException("image update failed");
        }
    }
}
