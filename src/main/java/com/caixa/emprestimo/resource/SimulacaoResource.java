package com.caixa.emprestimo.resource;

import com.caixa.emprestimo.dto.SimulacaoRequestDTO;
import com.caixa.emprestimo.dto.SimulacaoResponseDTO;
import com.caixa.emprestimo.entity.ProdutoEmprestimo;
import com.caixa.emprestimo.repository.ProdutoEmprestimoRepository;
import com.caixa.emprestimo.service.SimulacaoService;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

@Path("/simulacoes")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Simulações", description = "Operações para simular empréstimos")
public class SimulacaoResource {

    @Inject
    SimulacaoService simulacaoService;

    @Inject
    ProdutoEmprestimoRepository produtoRepository;

    @POST
    @Operation(summary = "Realizar uma simulação de empréstimo", description = "Calcula os detalhes de um empréstimo com base em um produto, valor e prazo.")
    @APIResponse(responseCode = "200", description = "Simulação calculada com sucesso.", content = @Content(mediaType = MediaType.APPLICATION_JSON, schema = @Schema(implementation = SimulacaoResponseDTO.class)))
    @APIResponse(responseCode = "400", description = "Prazo solicitado excede o limite do produto.")
    @APIResponse(responseCode = "404", description = "Produto não encontrado com o ID fornecido.")
    public Response simular(SimulacaoRequestDTO request) {

        ProdutoEmprestimo produto = produtoRepository.findById(request.getIdProduto());

        if (produto == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("{\"error\": \"Produto não encontrado.\"}")
                    .build();
        }

        if (request.getPrazoMeses() > produto.getPrazoMaximoMeses()) {
            String errorMessage = String.format("Prazo solicitado (%d meses) excede o limite do produto (%d meses).",
                    request.getPrazoMeses(), produto.getPrazoMaximoMeses());
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\": \"" + errorMessage + "\"}")
                    .build();
        }

        SimulacaoResponseDTO responseDTO = simulacaoService.realizarSimulacao(request, produto);

        return Response.ok(responseDTO).build();
    }
}