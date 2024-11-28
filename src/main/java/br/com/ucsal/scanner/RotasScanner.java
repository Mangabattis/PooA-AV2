package br.com.ucsal.scanner;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.ucsal.annotation.Rota;

public class RotasScanner {

    public static List<Method> scanRoutes(String packageName) throws ClassNotFoundException, IOException {
        List<Method> routeMethods = new ArrayList<>();

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
                        routeMethods.add(method);
                    }
                }
            }
        }

        return routeMethods;
    }
}

