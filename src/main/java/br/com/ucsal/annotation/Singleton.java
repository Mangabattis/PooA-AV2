package br.com.ucsal.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

// Define a anotação Singleton
@Retention(RetentionPolicy.RUNTIME) // Disponível em tempo de execução
@Target(ElementType.TYPE) // Pode ser usada em classes ou interfaces
public @interface Singleton {
}
