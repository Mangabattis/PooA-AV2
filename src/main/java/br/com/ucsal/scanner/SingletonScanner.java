package br.com.ucsal.scanner;

import br.com.ucsal.annotation.Singleton;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import br.com.ucsal.persistencia.SingletonMemoria;

public class SingletonScanner {

    public static void initializeSingletons(String packageName) throws ClassNotFoundException, IOException {
        // Obter todas as classes do pacote
        List<Class<?>> classes = findClassesInPackage(packageName);

        for (Class<?> clazz : classes) {
            // Verificar se a classe está anotada com @Singleton
            if (clazz.isAnnotationPresent(Singleton.class)) {
                try {
                    // Verificar se a classe tem um construtor padrão
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);

                    // Criar instância e registrar no SingletonManager
                    Object instance = constructor.newInstance();
                    SingletonMemoria.registerInstance(clazz, instance);
                } catch (Exception e) {
                    throw new RuntimeException("Erro ao registrar singleton: " + clazz.getName(), e);
                }
            }
        }
    }

    private static List<Class<?>> findClassesInPackage(String packageName) throws ClassNotFoundException, IOException {
        List<Class<?>> classes = new ArrayList<>();

        // Caminho do pacote
        String path = packageName.replace('.', '/');
        URL resource = Thread.currentThread().getContextClassLoader().getResource(path);

        if (resource == null) {
            throw new ClassNotFoundException("Pacote não encontrado: " + packageName);
        }

        // Obter o diretório
        File directory = new File(resource.getFile());
        if (!directory.exists()) {
            throw new ClassNotFoundException("Diretório do pacote não encontrado: " + packageName);
        }

        // Iterar pelos arquivos do diretório
        for (File file : directory.listFiles()) {
            if (file.getName().endsWith(".class")) {
                // Obter o nome completo da classe
                String className = packageName + '.' + file.getName().replace(".class", "");
                classes.add(Class.forName(className));
            }
        }

        return classes;
    }
}

