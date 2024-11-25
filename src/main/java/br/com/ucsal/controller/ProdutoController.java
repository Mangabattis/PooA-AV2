package br.com.ucsal.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import br.com.ucsal.annotation.Rota;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Define o servlet para capturar rotas relacionadas a "/produto/*"
@WebServlet("/produto/*")
public class ProdutoController extends HttpServlet {

    private Map<String, Method> rotaMap = new HashMap<>();
    private Map<Class<?>, Object> controllers = new HashMap<>();

    @Override
    public void init() throws ServletException {
        try {
            // Registra as rotas dinamicamente
            registrarRotas();
        } catch (Exception e) {
            throw new ServletException("Erro ao inicializar rotas dinâmicas.", e);
        }
    }

    private void registrarRotas() throws Exception {
        // Lista as classes de controle para escanear (adicione mais conforme necessário)
        Class<?>[] controllerClasses = {
            ProdutoRoutes.class // Classe onde estão os métodos anotados com @Rota
        };

        // Escaneia métodos anotados com @Rota
        for (Class<?> controllerClass : controllerClasses) {
            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
            controllers.put(controllerClass, controllerInstance);

            for (Method method : controllerClass.getDeclaredMethods()) {
                if (method.isAnnotationPresent(Rota.class)) {
                    Rota rota = method.getAnnotation(Rota.class);
                    rotaMap.put(rota.value(), method);
                }
            }
        }
    }

    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String path = request.getPathInfo(); // Obtém o path da requisição
        Method method = rotaMap.get(path); // Busca o método associado
        System.out.println("Metodo: "+ method);

        if (method != null) {
            try {
                Object controller = controllers.get(method.getDeclaringClass());
                method.invoke(controller, request, response); // Invoca o método associado à rota
            } catch (Exception e) {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Erro ao processar a rota.");
                e.printStackTrace();
            }
        } else {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "Rota não encontrada.");
        }
    }
}
