package com.caixa.emprestimo.dto;

import com.caixa.emprestimo.entity.ProdutoEmprestimo;

public class ProdutoDTO {
    private Long id;
    private String nome;
    private Double taxaJurosAnual;
    private Integer prazoMaximoMeses;

    public ProdutoDTO() {
    }

    public ProdutoDTO(ProdutoEmprestimo entity) {
        this.id = entity.getId();
        this.nome = entity.getNome();
        this.taxaJurosAnual = entity.getTaxaJurosAnual();
        this.prazoMaximoMeses = entity.getPrazoMaximoMeses();
    }

    public ProdutoDTO(ProdutoDTO outro) {
        this.id = outro.id;
        this.nome = outro.nome;
        this.taxaJurosAnual = outro.taxaJurosAnual;
        this.prazoMaximoMeses = outro.prazoMaximoMeses;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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