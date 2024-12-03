package br.com.ucsal.persistencia;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import br.com.ucsal.scanner.SingletonScanner;

public class SingletonMemoria {
    private static final Map<Class<?>, Object> INSTANCES = new HashMap<>();

    static {
        try {
            SingletonScanner.initializeSingletons("br.com.ucsal.persistencia"); // Chama o método que inicializa os Singletons, passando o pacote 
        } catch (ClassNotFoundException | IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar Singletons", e);
        }
    }

    public static void registerInstance(Class<?> clazz, Object instance) {
        INSTANCES.put(clazz, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        Object instance = INSTANCES.get(clazz);
        
        if (instance == null) {
            try {
                instance = clazz.getDeclaredConstructor().newInstance();
                INSTANCES.put(clazz, instance);
            } catch (Exception e) {
                throw new RuntimeException("Erro ao criar a instância para a classe " + clazz.getName(), e);
            }
        }
            
        return (T) instance;
    }
}
