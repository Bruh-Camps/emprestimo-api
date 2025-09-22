package com.caixa.emprestimo.resource;

import com.caixa.emprestimo.dto.ProdutoRequestDTO;
import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import com.caixa.emprestimo.repository.ProdutoEmprestimoRepository;
import io.quarkus.test.InjectMock;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Optional;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@QuarkusTest
class ProdutoEmprestimoResourceTest {

    @InjectMock
    private ProdutoEmprestimoRepository repository;

    @Test
    @DisplayName("Deve criar um novo produto com sucesso e retornar 201")
    void deveCriarUmNovoProduto() {

        ProdutoRequestDTO requestDTO = new ProdutoRequestDTO();
        requestDTO.setNome("Crédito Pessoal Teste");
        requestDTO.setTaxaJurosAnual(25.0);
        requestDTO.setPrazoMaximoMeses(36);

        doAnswer(invocation -> {
            ProdutoEmprestimo entity = invocation.getArgument(0);
            entity.setId(1L); // Simula que o banco de dados atribuiu o ID 1
            return null;     // Obrigatório para métodos void
        }).when(repository).persist(any(ProdutoEmprestimo.class));

        given()
                .contentType(ContentType.JSON)
                .body(requestDTO)
                .when()
                .post("/produtos")
                .then()
                .statusCode(201)
                // Agora podemos verificar o ID específico que simulamos
                .body("id", equalTo(1))
                .body("nome", equalTo("Crédito Pessoal Teste"));
    }

    @Test
    @DisplayName("Deve buscar um produto por ID e retornar 200")
    void deveBuscarProdutoPorId() {

        ProdutoEmprestimo produtoExistente = new ProdutoEmprestimo();
        produtoExistente.setId(1L);
        produtoExistente.setNome("Produto para Busca");
        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(produtoExistente));

        given()
                .when()
                .get("/produtos/1")
                .then()
                .statusCode(200)
                .body("id", equalTo(1))
                .body("nome", equalTo("Produto para Busca"));
    }

    @Test
    @DisplayName("Deve retornar 404 ao buscar um produto por ID que não existe")
    void deveRetornar404_QuandoBuscarIdInexistente() {

        when(repository.findByIdOptional(99L)).thenReturn(Optional.empty());

        given()
                .when()
                .get("/produtos/99")
                .then()
                .statusCode(404);
    }

    @Test
    @DisplayName("Deve listar os produtos e retornar 200")
    void deveListarTodosOsProdutos() {

        ProdutoEmprestimo produto = new ProdutoEmprestimo();
        produto.setId(1L);
        when(repository.listAll()).thenReturn(Collections.singletonList(produto));

        given()
                .when()
                .get("/produtos")
                .then()
                .statusCode(200)
                .body("size()", is(1))
                .body("[0].id", is(1));
    }

    @Test
    @DisplayName("Deve atualizar um produto existente e retornar 200")
    void deveAtualizarUmProdutoExistente() {

        ProdutoEmprestimo produtoExistente = new ProdutoEmprestimo();
        produtoExistente.setId(1L);
        when(repository.findByIdOptional(1L)).thenReturn(Optional.of(produtoExistente));

        ProdutoRequestDTO requestDTOAtualizado = new ProdutoRequestDTO();
        requestDTOAtualizado.setNome("Atualizado");
        requestDTOAtualizado.setTaxaJurosAnual(22.0);
        requestDTOAtualizado.setPrazoMaximoMeses(48);

        given()
                .contentType(ContentType.JSON)
                .body(requestDTOAtualizado)
                .when()
                .put("/produtos/1")
                .then()
                .statusCode(200)
                .body("nome", is("Atualizado"))
                .body("taxaJurosAnual", is(22.0f));
    }

    @Test
    @DisplayName("Deve remover um produto e retornar 204")
    void deveRemoverUmProduto() {

        when(repository.deleteById(1L)).thenReturn(true);

        given()
                .when()
                .delete("/produtos/1")
                .then()
                .statusCode(204);
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar remover um produto que não existe")
    void deveRetornar404_QuandoRemoverIdInexistente() {

        when(repository.deleteById(99L)).thenReturn(false);

        given()
                .when()
                .delete("/produtos/99")
                .then()
                .statusCode(404);
    }
}