package br.com.ucsal.persistencia;

import java.util.Map;
import java.util.HashMap;
import java.io.IOException;

import br.com.ucsal.scanner.SingletonScanner;

public class SingletonMemoria {
    private static final Map<Class<?>, Object> INSTANCES = new HashMap<>();

    static {
        try {
            // Chama o método que inicializa os Singletons, passando o pacote onde as classes anotadas com @Singleton estão localizadas
            SingletonScanner.initializeSingletons("br.com.ucsal.persistencia");
        } catch (ClassNotFoundException | IOException e) {
            // Em caso de erro, pode logar ou lançar uma exceção dependendo do seu controle de erro
            e.printStackTrace();
            throw new RuntimeException("Erro ao inicializar Singletons", e);
        }
    }

    // Registro de uma instância
    public static void registerInstance(Class<?> clazz, Object instance) {
        INSTANCES.put(clazz, instance);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getInstance(Class<T> clazz) {
        // Verifica se a instância já está registrada
        Object instance = INSTANCES.get(clazz);
        
        // Se a instância não existir, cria uma nova
        if (instance == null) {
            try {
                // Cria uma nova instância utilizando o construtor da classe
                instance = clazz.getDeclaredConstructor().newInstance();
                // Registra a nova instância no mapa
                INSTANCES.put(clazz, instance);
            } catch (Exception e) {
                // Em caso de erro (por exemplo, construtor sem argumentos ou falha na criação), lança uma exceção
                throw new RuntimeException("Erro ao criar a instância para a classe " + clazz.getName(), e);
            }
        }
            
        return (T) instance;  // Retorna a instância
    }
}
