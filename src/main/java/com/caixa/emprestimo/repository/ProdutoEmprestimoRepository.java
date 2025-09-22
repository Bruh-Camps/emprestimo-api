package com.caixa.emprestimo.repository;

import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ProdutoEmprestimoRepository implements PanacheRepository<ProdutoEmprestimo> {
}
