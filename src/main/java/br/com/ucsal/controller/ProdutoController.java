package br.com.ucsal.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ucsal.annotation.Rota;
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

    /**
     * Registra as rotas dinamicamente, buscando métodos anotados com @Rota no pacote especificado.
     */
    private void registrarRotas() throws Exception {
        String packageName = "br.com.ucsal.rotas"; // Pacote onde as rotas estão localizadas

        try {
            // Busca todas as classes no pacote manualmente
            List<Method> methods = RotasScanner.scanRoutes(packageName);
            
            for (Method method : methods) {
                Rota rota = method.getAnnotation(Rota.class);
                Class<?> declaringClass = method.getDeclaringClass();
                
                // Cria a instância do controlador se ainda não existir
                if (!controllers.containsKey(declaringClass)) {
                    Object controllerInstance = declaringClass.getDeclaredConstructor().newInstance();
                    controllers.put(declaringClass, controllerInstance);
                }

                // Registra o método e a rota
                rotaMap.put(rota.value(), method);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
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
