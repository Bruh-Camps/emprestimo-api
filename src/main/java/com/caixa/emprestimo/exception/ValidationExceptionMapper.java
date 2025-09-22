package com.caixa.emprestimo.exception;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Provider
public class ValidationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {

        List<Map<String, String>> errors = exception.getConstraintViolations().stream()
                .map(this::toErrorMap)
                .collect(Collectors.toList());

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(errors)
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private Map<String, String> toErrorMap(ConstraintViolation<?> violation) {

        String field = violation.getPropertyPath().toString();
        String[] pathParts = field.split("\\.");
        String fieldName = pathParts.length > 0 ? pathParts[pathParts.length - 1] : field;

        return Map.of("campo", fieldName, "erro", violation.getMessage());
    }
}