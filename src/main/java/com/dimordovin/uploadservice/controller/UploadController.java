package com.dimordovin.uploadservice.controller;

import com.dimordovin.uploadservice.service.ImageService;
import com.dimordovin.uploadservice.service.S3Service;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

@Path("/images")
public class UploadController {

    public static final String AWS_URL = System.getenv("AWS_URL");

    private S3Service s3Service = new S3Service();
    private ImageService imageService = new ImageService();

    @PUT
    public Response uploadFile(@QueryParam("filename") String filename, @QueryParam("id") String id,
                               @Context HttpServletRequest request, InputStream requestBody)
            throws URISyntaxException {

        if (filename == null || filename.isEmpty() || id == null || id.isEmpty()) {
            return Response.status(400).entity("id or filename is empty").build();
        }

        String s3Filename = id + "-" + filename;
        s3Service.save(requestBody, s3Filename);

        String url = UriBuilder.fromUri(AWS_URL + "/" + S3Service.S3BUCKET + "/").segment(s3Filename)
                .build().toString();
        imageService.updateImage(id, url);

        return Response.ok().location(new URI(ImageService.IMAGE_SERVICE_BASE_URI + "/api/images/" + id)).build();
    }

    @DELETE
    @Path("/{s3Filename}")
    public Response uploadFile(@PathParam("s3Filename") String s3Filename) {

        if (s3Filename == null || s3Filename.isEmpty()) {
            return Response.status(400).entity("s3Filename is empty").build();
        }

        s3Service.delete(s3Filename);

        return Response.noContent().build();
    }
}
