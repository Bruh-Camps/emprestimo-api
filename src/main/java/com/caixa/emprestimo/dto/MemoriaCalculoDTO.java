package com.caixa.emprestimo.dto;


import java.math.BigDecimal;

/**
 * DTO que representa uma única parcela na memória de cálculo da simulação.
 */
public class MemoriaCalculoDTO {
    private int mes; // Mês da parcela
    private BigDecimal saldoDevedorInicial; // Saldo devedor no início do mês
    private BigDecimal juros; // Valor dos juros no mês
    private BigDecimal amortizacao; // Valor da amortização no mês
    private BigDecimal saldoDevedorFinal; // Saldo devedor após o pagamento da parcela

    public MemoriaCalculoDTO(int mes, BigDecimal saldoDevedorInicial, BigDecimal juros, BigDecimal amortizacao, BigDecimal saldoDevedorFinal) {
        this.mes = mes;
        this.saldoDevedorInicial = saldoDevedorInicial;
        this.juros = juros;
        this.amortizacao = amortizacao;
        this.saldoDevedorFinal = saldoDevedorFinal;
    }

    public int getMes() {
        return mes;
    }

    public void setMes(int mes) {
        this.mes = mes;
    }

    public BigDecimal getSaldoDevedorInicial() {
        return saldoDevedorInicial;
    }

    public void setSaldoDevedorInicial(BigDecimal saldoDevedorInicial) {
        this.saldoDevedorInicial = saldoDevedorInicial;
    }

    public BigDecimal getJuros() {
        return juros;
    }

    public void setJuros(BigDecimal juros) {
        this.juros = juros;
    }

    public BigDecimal getAmortizacao() {
        return amortizacao;
    }

    public void setAmortizacao(BigDecimal amortizacao) {
        this.amortizacao = amortizacao;
    }

    public BigDecimal getSaldoDevedorFinal() {
        return saldoDevedorFinal;
    }

    public void setSaldoDevedorFinal(BigDecimal saldoDevedorFinal) {
        this.saldoDevedorFinal = saldoDevedorFinal;
    }
}