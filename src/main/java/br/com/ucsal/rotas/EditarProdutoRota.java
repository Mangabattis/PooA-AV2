package br.com.ucsal.rotas;

import java.io.IOException;
import br.com.ucsal.annotation.Rota;
import br.com.ucsal.controller.NewBaseRotas;
import br.com.ucsal.model.Produto;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class EditarProdutoRota extends NewBaseRotas {

    @Rota("/editarProduto")
    public void editarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            Integer id = Integer.parseInt(req.getParameter("id"));
            Produto produto = produtoService.obterProdutoPorId(id);
            req.setAttribute("produto", produto);
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/produtoformulario.jsp");
            dispatcher.forward(req, resp);
        } else if ("POST".equalsIgnoreCase(method)) {
            Integer id = Integer.parseInt(req.getParameter("id"));
            String nome = req.getParameter("nome");
            double preco = Double.parseDouble(req.getParameter("preco"));
            Produto produto = new Produto(id, nome, preco);
            produtoService.atualizarProduto(produto);
            resp.sendRedirect("listarProdutos");
        }
    }
}
