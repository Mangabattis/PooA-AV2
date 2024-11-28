
package br.com.ucsal.rotas;

import java.io.IOException;
import java.util.List;

import br.com.ucsal.annotation.Rota;
import br.com.ucsal.controller.NewBaseRotas;
import br.com.ucsal.model.Produto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class NewRotaDetalhesProduto extends NewBaseRotas {

	@Rota("/detalhesProduto")
    public void listarProdutos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Produto> produtos = produtoService.listarProdutos();
        req.setAttribute("produtos", produtos);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/produtodetalhes.jsp");
        dispatcher.forward(req, resp);
    }

}
