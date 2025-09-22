package com.caixa.emprestimo.exception;

import jakarta.annotation.Priority;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.jboss.logging.Logger;

import java.util.Map;

@Provider
@Priority(jakarta.ws.rs.Priorities.USER) // Este mapper genérico tem prioridade sobre os outros
public class GenericExceptionMapper implements ExceptionMapper<Exception> {

    private static final Logger LOG = Logger.getLogger(GenericExceptionMapper.class);

    @Override
    public Response toResponse(Exception exception) {

        LOG.error("Ocorreu um erro inesperado na aplicação", exception);

        Map<String, String> erroResponse = Map.of(
                "erro", "Ocorreu um erro interno no servidor. Por favor, tente novamente mais tarde."
        );

        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(erroResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}