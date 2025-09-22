package com.caixa.emprestimo.dto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class SimulacaoResponseDTO {
    private ProdutoDTO produto; // Dados do produto
    private BigDecimal valorSolicitado; // Valor solicitado
    private Integer prazoMeses; // Prazo em meses
    private Double taxaJurosEfetivaMensal; // Taxa de juros efetiva mensal
    private BigDecimal valorTotalComJuros; // Valor total com juros
    private BigDecimal parcelaMensal;
    private List<MemoriaCalculoDTO> memoriaCalculo; // Detalhamento mês a mês [cite: 7, 8]

    public ProdutoDTO getProduto() {
        return (this.produto == null) ? null : new ProdutoDTO(this.produto);
    }

    public void setProduto(ProdutoDTO produto) {
        this.produto = (produto == null) ? null : new ProdutoDTO(produto);
    }

    public BigDecimal getValorSolicitado() {
        return valorSolicitado;
    }

    public void setValorSolicitado(BigDecimal valorSolicitado) {
        this.valorSolicitado = valorSolicitado;
    }

    public Integer getPrazoMeses() {
        return prazoMeses;
    }

    public void setPrazoMeses(Integer prazoMeses) {
        this.prazoMeses = prazoMeses;
    }

    public Double getTaxaJurosEfetivaMensal() {
        return taxaJurosEfetivaMensal;
    }

    public void setTaxaJurosEfetivaMensal(Double taxaJurosEfetivaMensal) {
        this.taxaJurosEfetivaMensal = taxaJurosEfetivaMensal;
    }

    public BigDecimal getValorTotalComJuros() {
        return valorTotalComJuros;
    }

    public void setValorTotalComJuros(BigDecimal valorTotalComJuros) {
        this.valorTotalComJuros = valorTotalComJuros;
    }

    public BigDecimal getParcelaMensal() {
        return parcelaMensal;
    }

    public void setParcelaMensal(BigDecimal parcelaMensal) {
        this.parcelaMensal = parcelaMensal;
    }

    public List<MemoriaCalculoDTO> getMemoriaCalculo() {
        return (this.memoriaCalculo == null) ? null : new ArrayList<>(this.memoriaCalculo);
    }

    public void setMemoriaCalculo(List<MemoriaCalculoDTO> memoriaCalculo) {
        this.memoriaCalculo = (memoriaCalculo == null) ? null : new ArrayList<>(memoriaCalculo);
    }
}