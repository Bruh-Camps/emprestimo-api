package com.caixa.emprestimo.exception;

import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;
import org.hibernate.exception.ConstraintViolationException;
import org.jboss.logging.Logger;

import java.util.Map;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> { // 2. Especifica qual exceção este Mapper trata

    private static final Logger LOG = Logger.getLogger(ConstraintViolationExceptionMapper.class);

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        LOG.error("Ocorreu uma violação de constraint no banco de dados", exception);

        String mensagemUsuario = "Os dados enviados violam uma restrição do banco de dados. Verifique se o item já não existe.";

        if (exception.getErrorCode() == 23505) {
            mensagemUsuario = "Já existe um registro com o identificador fornecido.";
        }

        Map<String, String> erroResponse = Map.of("erro", mensagemUsuario);

        return Response.status(Response.Status.CONFLICT)
                .entity(erroResponse)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}