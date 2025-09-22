package com.caixa.emprestimo.exception;

import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.Map;

@Provider
public class NotFoundExceptionMapper implements ExceptionMapper<NotFoundException> {

    @Override
    public Response toResponse(NotFoundException exception) {
        Map<String, String> erroResponse = Map.of("erro", "O recurso solicitado não foi encontrado.");

        return Response.status(Response.Status.NOT_FOUND)
                .entity(erroResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}