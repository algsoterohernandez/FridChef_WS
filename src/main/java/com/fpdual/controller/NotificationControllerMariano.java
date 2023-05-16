package com.fpdual.controller;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import com.fpdual.api.dto.NotificationMariano;
public class NotificationControllerMariano {
    @GET
    public Response ping() {
        return Response.ok().entity("Service online").build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotification(@PathParam("id") int id) {
        return Response.ok().entity(new NotificationMariano(id, "john", "test notification")).build();
    }
    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    public Response getNotification(@PathParam("id") int id, @PathParam("name") String name) {
        return Response.ok().entity(new NotificationMariano(id, name, "test notification")).build();
    }
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public   Response   getNotificationWithParameters(@PathParam("id")   int   id,   @QueryParam("name") String name) {
        if (name == null || name.trim().isEmpty()) {
            return Response.status(400).entity("Name no present in the request").build();
        } else {
            return Response.ok().entity(new NotificationMariano(id, name, "test notification")).build();
        }
    }
    @GET
    @Produces(MediaType.APPLICATION_XML)public Response getNotificationXML(@PathParam("id") int id) {
        return Response.ok().entity(new NotificationMariano(id, "john", "test notification")).build();
    }
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response postNotification(NotificationMariano notification) {
        return Response.status(201).entity(notification).build();
    }
}