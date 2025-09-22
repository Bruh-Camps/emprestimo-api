package com.caixa.emprestimo.resource;

import com.caixa.emprestimo.dto.ProdutoRequestDTO;
import com.caixa.emprestimo.dto.ProdutoResponseDTO;
import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import com.caixa.emprestimo.repository.ProdutoEmprestimoRepository;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/produtos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProdutoEmprestimoResource {

    @Inject
    ProdutoEmprestimoRepository repository;

    @POST
    @Transactional
    public Response criar(@Valid ProdutoRequestDTO requestDTO) {

        ProdutoEmprestimo novoProduto = new ProdutoEmprestimo();
        novoProduto.setNome(requestDTO.getNome());
        novoProduto.setTaxaJurosAnual(requestDTO.getTaxaJurosAnual());
        novoProduto.setPrazoMaximoMeses(requestDTO.getPrazoMaximoMeses());

        repository.persist(novoProduto);

        ProdutoResponseDTO responseDTO = new ProdutoResponseDTO(novoProduto);
        return Response.status(Response.Status.CREATED).entity(responseDTO).build();
    }

    @GET
    public List<ProdutoResponseDTO> listar() {

        return repository.listAll().stream()
                .map(ProdutoResponseDTO::new)
                .collect(Collectors.toList());
    }

    @GET
    @Path("/{id}")
    public Response buscarPorId(@PathParam("id") Long id) {
        return repository.findByIdOptional(id)
                .map(produto -> Response.ok(new ProdutoResponseDTO(produto)).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response atualizar(@PathParam("id") Long id, @Valid ProdutoRequestDTO requestDTO) {
        return repository.findByIdOptional(id)
                .map(produtoExistente -> {
                    produtoExistente.setNome(requestDTO.getNome());
                    produtoExistente.setTaxaJurosAnual(requestDTO.getTaxaJurosAnual());
                    produtoExistente.setPrazoMaximoMeses(requestDTO.getPrazoMaximoMeses());

                    return Response.ok(new ProdutoResponseDTO(produtoExistente)).build();
                })
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response remover(@PathParam("id") Long id) {
        boolean deletado = repository.deleteById(id);
        if (deletado) {
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }
}