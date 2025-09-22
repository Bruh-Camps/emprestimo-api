package com.caixa.emprestimo.service;

import com.caixa.emprestimo.dto.*;
import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import jakarta.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class SimulacaoService {

    // Define a precisão para cálculos monetários e taxas
    private static final int MONETARY_SCALE = 2;
    private static final int RATE_SCALE = 10;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Orquestra a simulação completa do empréstimo.
     *
     * @param request O DTO com os dados da solicitação.
     * @param produto A entidade do produto de empréstimo.
     * @return Um DTO com o resultado completo da simulação.
     */
    public SimulacaoResponseDTO realizarSimulacao(SimulacaoRequestDTO request, ProdutoEmprestimo produto) {
        BigDecimal valorSolicitado = request.getValorSolicitado();
        int prazoMeses = request.getPrazoMeses();

        double taxaAnual = produto.getTaxaJurosAnual();
        double taxaMensalDecimal = calcularTaxaEfetivaMensal(taxaAnual);
        BigDecimal taxaMensal = BigDecimal.valueOf(taxaMensalDecimal);

        //calcula parcela mensal (Sistema Price)
        BigDecimal parcelaMensal = calcularParcelaMensal(valorSolicitado, taxaMensal, prazoMeses);

        //gera tabela de amortização
        List<MemoriaCalculoDTO> memoriaCalculo = gerarMemoriaDeCalculo(valorSolicitado, taxaMensal, prazoMeses, parcelaMensal);

        SimulacaoResponseDTO response = new SimulacaoResponseDTO();
        response.setProduto(new ProdutoDTO(produto));
        response.setValorSolicitado(valorSolicitado);
        response.setPrazoMeses(prazoMeses);
        response.setTaxaJurosEfetivaMensal(taxaMensalDecimal);
        response.setParcelaMensal(parcelaMensal);
        response.setValorTotalComJuros(parcelaMensal.multiply(BigDecimal.valueOf(prazoMeses)).setScale(MONETARY_SCALE, ROUNDING_MODE));
        response.setMemoriaCalculo(memoriaCalculo);

        return response;
    }

    /**
     * Gera a memória de cálculo mês a mês (Tabela Price).
     *
     * @param valorSolicitado O valor principal do empréstimo.
     * @param taxaMensal A taxa de juros mensal efetiva (em decimal).
     * @param prazoMeses O número de parcelas.
     * @param parcelaMensal O valor da parcela fixa.
     * @return Uma lista de DTOs, cada um representando uma linha da tabela.
     */
    private List<MemoriaCalculoDTO> gerarMemoriaDeCalculo(BigDecimal valorSolicitado, BigDecimal taxaMensal, int prazoMeses, BigDecimal parcelaMensal) {
        List<MemoriaCalculoDTO> memoria = new ArrayList<>();
        BigDecimal saldoDevedorAtual = valorSolicitado;

        for (int mes = 1; mes <= prazoMeses; mes++) {
            BigDecimal saldoDevedorInicial = saldoDevedorAtual;

            BigDecimal juros = saldoDevedorInicial.multiply(taxaMensal).setScale(MONETARY_SCALE, ROUNDING_MODE);

            BigDecimal amortizacao = parcelaMensal.subtract(juros);

            if (mes == prazoMeses) {
                if (amortizacao.compareTo(saldoDevedorInicial) > 0) {
                    amortizacao = saldoDevedorInicial;
                }
            }

            BigDecimal saldoDevedorFinal = saldoDevedorInicial.subtract(amortizacao);

            if (mes == prazoMeses) {
                saldoDevedorFinal = BigDecimal.ZERO;
            }

            MemoriaCalculoDTO calculoMes = new MemoriaCalculoDTO(
                    mes,
                    saldoDevedorInicial.setScale(MONETARY_SCALE, ROUNDING_MODE),
                    juros.setScale(MONETARY_SCALE, ROUNDING_MODE),
                    amortizacao.setScale(MONETARY_SCALE, ROUNDING_MODE),
                    saldoDevedorFinal.setScale(MONETARY_SCALE, ROUNDING_MODE)
            );
            memoria.add(calculoMes);

            saldoDevedorAtual = saldoDevedorFinal;
        }

        return memoria;
    }

    /**
     * Calcula a taxa de juros efetiva mensal a partir da taxa anual.
     * Fórmula: i = (1 + taxa anual)^(1/12) - 1
     */
    private double calcularTaxaEfetivaMensal(double taxaAnualPercentual) {
        double a = (taxaAnualPercentual/100) + 1;
        double b = Math.pow(a, (1.0/12.0));
        return (b - 1);
    }

    /**
     * Calcula o valor de uma parcela fixa (Sistema Price).
     * Fórmula: PMT = PV * [i * (1 + i)^n] / [(1 + i)^n - 1]
     */
    private BigDecimal calcularParcelaMensal(BigDecimal valorSolicitado, BigDecimal taxaMensal, int prazoMeses) {
        // (1 + i)^n
        BigDecimal fator = BigDecimal.ONE.add(taxaMensal).pow(prazoMeses);

        // i * (1 + i)^n
        BigDecimal numerador = taxaMensal.multiply(fator);

        // (1 + i)^n - 1
        BigDecimal denominador = fator.subtract(BigDecimal.ONE);

        if (denominador.compareTo(BigDecimal.ZERO) == 0) {
            return valorSolicitado.divide(BigDecimal.valueOf(prazoMeses), MONETARY_SCALE, ROUNDING_MODE);
        }

        return valorSolicitado.multiply(numerador).divide(denominador, MONETARY_SCALE, ROUNDING_MODE);
    }
}