package br.com.ucsal.persistencia;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

import br.com.ucsal.annotation.Inject;

public class ContainerInjector {
	
	 private static final int TIPO_PERSISTENCIA = PersistenciaFactory.HSQL; // pode mudar para memória tbm (PersistenciaFactory.MEMORIA)

	    public static void injetarDependencias(Object objeto) {
	        Field[] campos = objeto.getClass().getDeclaredFields();

	        for (Field campo : campos) {
	            if (campo.isAnnotationPresent((Class<? extends Annotation>) Inject.class)) {
	                try {
	                    Object repositorio = PersistenciaFactory.getProdutoRepository(TIPO_PERSISTENCIA);
	                    campo.setAccessible(true); //Vai acessar o campo privado
	                    campo.set(objeto, repositorio);
	                    
	                    if (repositorio instanceof HSQLProdutoRepository) {
	                        System.out.println("Repositório HSQLDB foi injetado!");
	                    } else if (repositorio instanceof MemoriaProdutoRepository) {
	                        System.out.println("Repositório em memória foi injetado!");
	                    }
	                } catch (IllegalAccessException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	    }

}
