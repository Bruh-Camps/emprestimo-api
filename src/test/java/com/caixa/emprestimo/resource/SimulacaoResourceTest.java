package com.caixa.emprestimo.resource;

import com.caixa.emprestimo.dto.SimulacaoRequestDTO;
import com.caixa.emprestimo.dto.SimulacaoResponseDTO;
import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import com.caixa.emprestimo.repository.ProdutoEmprestimoRepository;
import com.caixa.emprestimo.service.SimulacaoService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@QuarkusTest
class SimulacaoResourceTest {

    @InjectMock
    private SimulacaoService simulacaoService;

    @InjectMock
    private ProdutoEmprestimoRepository produtoRepository;

    @Test
    @DisplayName("Deve retornar 200 OK com a simulação quando a requisição é válida")
    void deveRetornar200_QuandoRequisicaoValida() {

        SimulacaoRequestDTO request = new SimulacaoRequestDTO();
        request.setIdProduto(1L);
        request.setPrazoMeses(12);
        request.setValorSolicitado(new BigDecimal("10000.00"));

        ProdutoEmprestimo produtoValido = new ProdutoEmprestimo();
        produtoValido.setId(1L);
        produtoValido.setPrazoMaximoMeses(24);

        when(produtoRepository.findById(1L)).thenReturn(produtoValido);
        when(simulacaoService.realizarSimulacao(any(SimulacaoRequestDTO.class), any(ProdutoEmprestimo.class)))
                .thenReturn(new SimulacaoResponseDTO());

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(200)
                .body(notNullValue());
    }

    @Test
    @DisplayName("Deve retornar 404 Not Found quando o produto não existe")
    void deveRetornar404_QuandoProdutoNaoExiste() {

        SimulacaoRequestDTO request = new SimulacaoRequestDTO();
        request.setIdProduto(99L); // ID que não existe

        when(produtoRepository.findById(99L)).thenReturn(null);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando o prazo solicitado excede o limite do produto")
    void deveRetornar400_QuandoPrazoExcedeLimite() {

        SimulacaoRequestDTO request = new SimulacaoRequestDTO();
        request.setIdProduto(1L);
        request.setPrazoMeses(30); // Prazo maior que o permitido

        ProdutoEmprestimo produtoValido = new ProdutoEmprestimo();
        produtoValido.setId(1L);
        produtoValido.setPrazoMaximoMeses(24); // Limite do produto é 24

        when(produtoRepository.findById(1L)).thenReturn(produtoValido);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/simulacoes")
                .then()
                .statusCode(400)
                .body("error", containsString("excede o limite do produto"));
    }
}