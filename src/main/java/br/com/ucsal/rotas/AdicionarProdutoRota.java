package br.com.ucsal.rotas;

import java.io.IOException;
import br.com.ucsal.annotation.Rota;
import br.com.ucsal.controller.NewBaseRotas;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class AdicionarProdutoRota extends NewBaseRotas {

    @Rota("/adicionarProduto")
    public void adicionarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String method = req.getMethod();
        if ("GET".equalsIgnoreCase(method)) {
            RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/produtoformulario.jsp");
            dispatcher.forward(req, resp);
        } else if ("POST".equalsIgnoreCase(method)) {
            String nome = req.getParameter("nome");
            double preco = Double.parseDouble(req.getParameter("preco"));
            produtoService.adicionarProduto(nome, preco);
            resp.sendRedirect("listarProdutos");
        }
    }
}
