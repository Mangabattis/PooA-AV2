package br.com.ucsal.persistencia;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import br.com.ucsal.annotation.Singleton;

public class SingletonMemoria {
    private static final Map<Class<?>, Object> INSTANCES = new HashMap<>();

    @SuppressWarnings("unchecked")
	public static <T> T getSingleton(Class<T> clazz) {
        return (T) INSTANCES.computeIfAbsent(clazz, SingletonMemoria::createInstance);
    }

    private static <T> T createInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            constructor.setAccessible(true); // Permite acesso ao construtor privado
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar instância para a classe: " + clazz.getName(), e);
        }
    }
}

