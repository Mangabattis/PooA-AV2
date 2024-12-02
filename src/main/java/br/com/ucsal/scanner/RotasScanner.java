package br.com.ucsal.scanner;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import br.com.ucsal.annotation.Rota;

public class RotasScanner {

    public static Map<String, Method> scanAndRegisterRoutes(String packageName, Map<Class<?>, Object> controllers) throws ClassNotFoundException, IOException {
        Map<String, Method> rotaMap = new HashMap<>();

        // Obter o diretório do pacote
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);
        if (resource == null) {
            throw new ClassNotFoundException("Pacote não encontrado: " + packageName);
        }

        // Listar arquivos no diretório
        File directory = new File(resource.getFile());
        if (!directory.exists()) {
            throw new ClassNotFoundException("Diretório do pacote não existe: " + packageName);
        }

        // Iterar por todos os arquivos .class no diretório
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                // Nome da classe completa
                String className = packageName + '.' + file.getName().replace(".class", "");
                Class<?> clazz = Class.forName(className);

                // Procurar métodos anotados com @Rota
                for (Method method : clazz.getDeclaredMethods()) {
                    if (method.isAnnotationPresent(Rota.class)) {
                        Rota rota = method.getAnnotation(Rota.class);
                        // Criar a instância do controlador se não existir
                        if (!controllers.containsKey(clazz)) {
                            try {
                                Object controllerInstance = clazz.getDeclaredConstructor().newInstance();
                                controllers.put(clazz, controllerInstance);
                            } catch (Exception e) {
                                e.printStackTrace();
                                throw new RuntimeException("Erro ao instanciar controlador: " + clazz.getName());
                            }
                        }

                        // Registra o método e a rota
                        rotaMap.put(rota.value(), method);
                    }
                }
            }
        }

        return rotaMap;
    }
}
