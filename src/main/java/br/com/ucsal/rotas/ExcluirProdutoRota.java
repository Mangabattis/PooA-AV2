package br.com.ucsal.rotas;

import java.io.IOException;
import br.com.ucsal.annotation.Rota;
import br.com.ucsal.controller.NewBaseRotas;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ExcluirProdutoRota extends NewBaseRotas {

    @Rota("/excluirProduto")
    public void excluirProduto(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        produtoService.removerProduto(id);
        resp.sendRedirect("listarProdutos");
    }
}
