package br.com.ucsal.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import br.com.ucsal.scanner.RotasScanner;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/produto/*")
public class ProdutoController extends HttpServlet {

    private Map<String, Method> rotaMap = new HashMap<>();
    private Map<Class<?>, Object> controllers = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {
            registrarRotas();
        } catch (Exception e) {
            throw new ServletException("Erro ao inicializar rotas dinâmicas.", e);
        }
    }
    
    private void registrarRotas() throws Exception {
        String packageName = "br.com.ucsal.rotas";

        Map<String, Method> routes = RotasScanner.scanAndRegisterRoutes(packageName, controllers);

        rotaMap.putAll(routes);
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo(); // Obtém o path da requisição

        Method method = rotaMap.get(path); // Busca o método associado
        System.out.println("Método " + method);

        if (method != null) {
            try {
                Object controller = controllers.get(method.getDeclaringClass());
                method.invoke(controller, request, response); // Invoca o método associado à rota
            } catch (Exception e) {
                e.printStackTrace();
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a rota.");
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota não encontrada.");
        }
    }
}
