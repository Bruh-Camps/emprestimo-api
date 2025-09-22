package com.caixa.emprestimo.exception;

import io.quarkus.test.junit.QuarkusTest; // 1. Adicionar esta anotação
import jakarta.inject.Inject; // Import para injeção
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import jakarta.ws.rs.core.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@QuarkusTest
class ValidationExceptionMapperTest {

    @Inject
    ValidationExceptionMapper mapper;

    @Test
    @DisplayName("Deve mapear uma única violação de validação para uma resposta 400")
    void deveMapearUmaUnicaViolacao() {

        Path mockPath = mock(Path.class);
        when(mockPath.toString()).thenReturn("criar.requestDTO.nome");

        ConstraintViolation<?> mockViolation = mock(ConstraintViolation.class);
        when(mockViolation.getMessage()).thenReturn("não pode estar em branco");
        when(mockViolation.getPropertyPath()).thenReturn(mockPath);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(mockViolation);

        ConstraintViolationException exception = new ConstraintViolationException("Erro de validação", violations);

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        List<Map<String, String>> errors = (List<Map<String, String>>) response.getEntity();
        assertEquals(1, errors.size());
        assertEquals("nome", errors.get(0).get("campo"));
        assertEquals("não pode estar em branco", errors.get(0).get("erro"));
    }

    @Test
    @DisplayName("Deve mapear múltiplas violações de validação para uma resposta 400")
    void deveMapearMultiplasViolacoes() {

        Path mockPath1 = mock(Path.class);
        when(mockPath1.toString()).thenReturn("prazoMaximoMeses");
        ConstraintViolation<?> mockViolation1 = mock(ConstraintViolation.class);
        when(mockViolation1.getMessage()).thenReturn("deve ser positivo");
        when(mockViolation1.getPropertyPath()).thenReturn(mockPath1);

        Path mockPath2 = mock(Path.class);
        when(mockPath2.toString()).thenReturn("taxaJurosAnual");
        ConstraintViolation<?> mockViolation2 = mock(ConstraintViolation.class);
        when(mockViolation2.getMessage()).thenReturn("não pode ser nulo");
        when(mockViolation2.getPropertyPath()).thenReturn(mockPath2);

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        violations.add(mockViolation1);
        violations.add(mockViolation2);

        ConstraintViolationException exception = new ConstraintViolationException(violations);

        Response response = mapper.toResponse(exception);

        assertEquals(Response.Status.BAD_REQUEST.getStatusCode(), response.getStatus());
        List<Map<String, String>> errors = (List<Map<String, String>>) response.getEntity();
        assertEquals(2, errors.size());
    }
}