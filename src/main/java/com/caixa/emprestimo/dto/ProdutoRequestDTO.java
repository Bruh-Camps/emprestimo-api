package com.caixa.emprestimo.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public class ProdutoRequestDTO {

    @NotBlank(message = "O nome do produto é obrigatório")
    private String nome;

    @NotNull(message = "A taxa de juros anual é obrigatória")
    @Positive(message = "A taxa de juros deve ser positiva")
    private Double taxaJurosAnual;

    @NotNull(message = "O prazo máximo é obrigatório")
    @Positive(message = "O prazo máximo deve ser positivo")
    private Integer prazoMaximoMeses;

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getTaxaJurosAnual() {
        return taxaJurosAnual;
    }

    public void setTaxaJurosAnual(Double taxaJurosAnual) {
        this.taxaJurosAnual = taxaJurosAnual;
    }

    public Integer getPrazoMaximoMeses() {
        return prazoMaximoMeses;
    }

    public void setPrazoMaximoMeses(Integer prazoMaximoMeses) {
        this.prazoMaximoMeses = prazoMaximoMeses;
    }
}