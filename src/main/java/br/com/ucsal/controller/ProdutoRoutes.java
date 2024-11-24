package br.com.ucsal.controller;

import java.io.IOException;
import java.util.List;

import br.com.ucsal.annotation.Rota;
import br.com.ucsal.model.Produto;
import br.com.ucsal.persistencia.HSQLProdutoRepository;
import br.com.ucsal.service.ProdutoService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class ProdutoRoutes {

    private final ProdutoService produtoService;

    public ProdutoRoutes() {
        HSQLProdutoRepository repository = new HSQLProdutoRepository();
        this.produtoService = new ProdutoService(repository);
    }
    
    @Rota("/")
    public void redirecionarParaListarProdutos(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.sendRedirect("listarProdutos");
    }
    
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
        	System.out.println("Editando produto");
            Integer id = Integer.parseInt(req.getParameter("id"));
            String nome = req.getParameter("nome");
            double preco = Double.parseDouble(req.getParameter("preco"));
            Produto produto = new Produto(id, nome, preco);
            produtoService.atualizarProduto(produto);
            resp.sendRedirect("listarProdutos");
        }
    }

    @Rota("/adicionarProduto")
    public void adicionarProduto(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	System.out.println("Adicionando produto");
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

    @Rota("/excluirProduto")
    public void excluirProduto(HttpServletRequest req, HttpServletResponse resp) throws IOException {
    	System.out.println("Excluindo produto");
        Integer id = Integer.parseInt(req.getParameter("id"));
        produtoService.removerProduto(id);
        resp.sendRedirect("listarProdutos");
    }

    @Rota("/listarProdutos")
    public void listarProdutos(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    	System.out.println("Listando produto");
        List<Produto> produtos = produtoService.listarProdutos();
        req.setAttribute("produtos", produtos);
        RequestDispatcher dispatcher = req.getRequestDispatcher("/WEB-INF/views/produtolista.jsp");
        dispatcher.forward(req, resp);
    }
}
