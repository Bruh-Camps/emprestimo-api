package com.caixa.emprestimo.dto;

import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class DtoTest {

    @Test
    @DisplayName("ProdutoRequestDTO deve guardar e retornar os dados corretamente")
    void testProdutoRequestDTO() {
        ProdutoRequestDTO dto = new ProdutoRequestDTO();
        dto.setNome("Teste");
        dto.setPrazoMaximoMeses(12);
        dto.setTaxaJurosAnual(10.0);

        assertEquals("Teste", dto.getNome());
        assertEquals(12, dto.getPrazoMaximoMeses());
        assertEquals(10.0, dto.getTaxaJurosAnual());
    }

    @Test
    @DisplayName("SimulacaoRequestDTO deve guardar e retornar os dados corretamente")
    void testSimulacaoRequestDTO() {
        SimulacaoRequestDTO dto = new SimulacaoRequestDTO();
        dto.setIdProduto(1L);
        dto.setPrazoMeses(24);
        dto.setValorSolicitado(new BigDecimal("5000.00"));

        assertEquals(1L, dto.getIdProduto());
        assertEquals(24, dto.getPrazoMeses());
        assertEquals(0, new BigDecimal("5000.00").compareTo(dto.getValorSolicitado()));
    }

    @Test
    @DisplayName("ProdutoDTO deve ser construído a partir de uma entidade e de outro DTO")
    void testProdutoDTO() {

        ProdutoEmprestimo entidade = new ProdutoEmprestimo();
        entidade.setId(1L);
        entidade.setNome("Produto Entidade");
        entidade.setPrazoMaximoMeses(12);
        entidade.setTaxaJurosAnual(15.0);

        ProdutoDTO dto1 = new ProdutoDTO(entidade);
        assertEquals(1L, dto1.getId());
        assertEquals("Produto Entidade", dto1.getNome());
        assertEquals(12, dto1.getPrazoMaximoMeses());
        assertEquals(15.0, dto1.getTaxaJurosAnual());

        ProdutoDTO dto2 = new ProdutoDTO(dto1);
        assertEquals(dto1.getId(), dto2.getId());
        assertEquals(dto1.getNome(), dto2.getNome());
    }

    @Test
    @DisplayName("MemoriaCalculoDTO deve guardar os dados do construtor corretamente")
    void testMemoriaCalculoDTO() {
        MemoriaCalculoDTO dto = new MemoriaCalculoDTO(
                1,
                new BigDecimal("1000.00"),
                new BigDecimal("10.00"),
                new BigDecimal("90.00"),
                new BigDecimal("910.00")
        );

        assertEquals(1, dto.getMes());
        assertEquals(0, new BigDecimal("1000.00").compareTo(dto.getSaldoDevedorInicial()));
        assertEquals(0, new BigDecimal("10.00").compareTo(dto.getJuros()));
        assertEquals(0, new BigDecimal("90.00").compareTo(dto.getAmortizacao()));
        assertEquals(0, new BigDecimal("910.00").compareTo(dto.getSaldoDevedorFinal()));
    }

    @Test
    @DisplayName("SimulacaoResponseDTO deve fazer cópias defensivas de objetos mutáveis")
    void testSimulacaoResponseDTO() {
        SimulacaoResponseDTO dto = new SimulacaoResponseDTO();

        ProdutoDTO produtoOriginal = new ProdutoDTO();
        produtoOriginal.setNome("Produto Original");
        dto.setProduto(produtoOriginal);

        produtoOriginal.setNome("Produto Modificado");

        assertNotEquals("Produto Modificado", dto.getProduto().getNome());
        assertEquals("Produto Original", dto.getProduto().getNome());


        List<MemoriaCalculoDTO> listaOriginal = new java.util.ArrayList<>();
        listaOriginal.add(new MemoriaCalculoDTO(1, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO));

        dto.setMemoriaCalculo(listaOriginal);

        List<MemoriaCalculoDTO> listaRetornada = dto.getMemoriaCalculo();

        listaRetornada.add(new MemoriaCalculoDTO(2, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE));

        assertEquals(1, listaOriginal.size(), "A lista original não deveria ter sido modificada.");
        assertEquals(2, listaRetornada.size(), "A lista retornada (a cópia) deveria ter sido modificada.");
        assertNotSame(listaOriginal, listaRetornada, "O getter deveria retornar uma cópia da lista, não a mesma instância.");
    }

}