package br.com.ucsal.controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import br.com.ucsal.annotation.Rota;
import br.com.ucsal.rotas.AdicionarProdutoRota;
import br.com.ucsal.rotas.EditarProdutoRota;
import br.com.ucsal.rotas.ExcluirProdutoRota;
import br.com.ucsal.rotas.ListarProdutosRota;
import br.com.ucsal.rotas.NewRotaDetalhesProduto;
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
    
    //novo
    private void registrarRotas() throws Exception {
        Class<?>[] controllerClasses = {
            AdicionarProdutoRota.class,
            EditarProdutoRota.class,
            ExcluirProdutoRota.class,
            ListarProdutosRota.class,
            NewRotaDetalhesProduto.class // Nova rota adicionada
        };

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

    
    
    //novo

  //Antes de modificar
//    private void registrarRotas() throws Exception {
//        Class<?>[] controllerClasses = {
//            ProdutoRoutes.class // Classe onde estão os métodos anotados com @Rota
//        };
//
//        // Escaneia métodos anotados com @Rota
//        for (Class<?> controllerClass : controllerClasses) {
//            Object controllerInstance = controllerClass.getDeclaredConstructor().newInstance();
//            controllers.put(controllerClass, controllerInstance);
//
//            for (Method method : controllerClass.getDeclaredMethods()) {
//                if (method.isAnnotationPresent(Rota.class)) {
//                    Rota rota = method.getAnnotation(Rota.class);
//                    rotaMap.put(rota.value(), method);
//                }
//            }
//        }
//    }
    //Antes de modificar

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
