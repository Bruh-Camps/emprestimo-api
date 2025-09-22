package com.caixa.emprestimo.service;

import com.caixa.emprestimo.dto.SimulacaoRequestDTO;
import com.caixa.emprestimo.dto.SimulacaoResponseDTO;
import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@DisplayName("Testes para o Serviço de Simulação de Empréstimo")
public class SimulacaoServiceTest {

    @Inject
    SimulacaoService simulacaoService;

    private SimulacaoResponseDTO response; // Armazena o resultado da simulação para ser usado em todos os testes

    @BeforeEach
    void setup() {
        // preparando o cenário para os testes
        ProdutoEmprestimo produto = new ProdutoEmprestimo();
        produto.setId(1L);
        produto.setNome("Empréstimo Pessoal");
        produto.setTaxaJurosAnual(18.0);
        produto.setPrazoMaximoMeses(24);

        SimulacaoRequestDTO request = new SimulacaoRequestDTO();
        request.setValorSolicitado(new BigDecimal("10000.00"));
        request.setPrazoMeses(12);

        this.response = simulacaoService.realizarSimulacao(request, produto);
    }

    @Test
    @DisplayName("Deve calcular a parcela mensal corretamente")
    void deveCalcularParcelaMensalCorretamente() {
        BigDecimal parcelaEsperada = new BigDecimal("910.46");
        assertEquals(0, parcelaEsperada.compareTo(response.getParcelaMensal()));
    }

    @Test
    @DisplayName("Deve calcular o valor total com juros corretamente")
    void deveCalcularValorTotalComJurosCorretamente() {
        BigDecimal valorTotalEsperado = new BigDecimal("10925.52");
        assertEquals(0, valorTotalEsperado.compareTo(response.getValorTotalComJuros()));
    }

    @Test
    @DisplayName("Deve gerar a memória de cálculo com o número correto de parcelas")
    void deveGerarMemoriaDeCalculoCompleta() {
        assertNotNull(response.getMemoriaCalculo());
        assertEquals(12, response.getMemoriaCalculo().size());
    }

    @Test
    @DisplayName("Deve zerar o saldo devedor na última parcela")
    void deveZerarSaldoDevedorAoFinal() {
        // A validação mais CRÍTICA de todas
        BigDecimal saldoFinalUltimaParcela = response.getMemoriaCalculo().get(11).getSaldoDevedorFinal();
        assertEquals(0, BigDecimal.ZERO.compareTo(saldoFinalUltimaParcela));
    }

    @Test
    @DisplayName("Deve calcular os juros e amortização da primeira parcela corretamente")
    void deveCalcularPrimeiraParcelaCorretamente() {
        var primeiraParcela = response.getMemoriaCalculo().get(0);
        assertEquals(0, new BigDecimal("10000.00").compareTo(primeiraParcela.getSaldoDevedorInicial()));
        assertEquals(0, new BigDecimal("138.88").compareTo(primeiraParcela.getJuros()));
        assertEquals(0, new BigDecimal("771.58").compareTo(primeiraParcela.getAmortizacao()));
    }

    @Test
    @DisplayName("Deve calcular a parcela sem juros quando a taxa anual é zero")
    void deveCalcularParcelaSemJuros_QuandoTaxaAnualEhZero() {
        // --- Arrange (Preparação) ---
        // Cria um produto com taxa de juros zero
        ProdutoEmprestimo produtoSemJuros = new ProdutoEmprestimo();
        produtoSemJuros.setId(2L);
        produtoSemJuros.setNome("Empréstimo Sem Juros");
        produtoSemJuros.setTaxaJurosAnual(0.0); // A condição para o denominador ser zero
        produtoSemJuros.setPrazoMaximoMeses(24);

        SimulacaoRequestDTO request = new SimulacaoRequestDTO();
        request.setValorSolicitado(new BigDecimal("12000.00"));
        request.setPrazoMeses(12);

        SimulacaoResponseDTO response = simulacaoService.realizarSimulacao(request, produtoSemJuros);

        BigDecimal parcelaEsperada = new BigDecimal("1000.00");
        assertEquals(0, parcelaEsperada.compareTo(response.getParcelaMensal()));

        assertEquals(0, request.getValorSolicitado().compareTo(response.getValorTotalComJuros()));

        assertEquals(0, BigDecimal.ZERO.compareTo(response.getMemoriaCalculo().get(0).getJuros()));
    }

    // Em src/test/java/com/caixa/emprestimo/service/SimulacaoServiceTest.java

    @Test
    @DisplayName("Deve ajustar a amortização final quando o arredondamento causa um valor excessivo")
    void deveAjustarAmortizacaoFinal_QuandoArredondamentoCausaExcesso() {
        ProdutoEmprestimo produto = new ProdutoEmprestimo();
        produto.setTaxaJurosAnual(10.0);
        produto.setPrazoMaximoMeses(12);

        SimulacaoRequestDTO request = new SimulacaoRequestDTO();
        request.setValorSolicitado(new BigDecimal("100.00"));
        request.setPrazoMeses(3);

        SimulacaoResponseDTO response = simulacaoService.realizarSimulacao(request, produto);

        assertNotNull(response);
        assertNotNull(response.getMemoriaCalculo());
        assertEquals(3, response.getMemoriaCalculo().size());

        var ultimaParcela = response.getMemoriaCalculo().get(2);

        assertEquals(0, ultimaParcela.getSaldoDevedorInicial().compareTo(ultimaParcela.getAmortizacao()),
                "A amortização da última parcela deveria ter sido ajustada para igualar o saldo devedor.");

        assertEquals(0, BigDecimal.ZERO.compareTo(ultimaParcela.getSaldoDevedorFinal()),
                "O saldo devedor final da última parcela deve ser ZERO.");
    }
}