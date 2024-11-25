package br.com.ucsal.persistencia;

public class TesteSingleton {
	 public static void main(String[] args) {
		 
		 MemoriaProdutoRepository instancia1 = SingletonMemoria.getSingleton(MemoriaProdutoRepository.class);
	     MemoriaProdutoRepository instancia2 = SingletonMemoria.getSingleton(MemoriaProdutoRepository.class);
	     
	     if (instancia1 == instancia2) {
	            System.out.println("Singleton está funcionando.");
	        } else {
	            System.out.println("O Singleton não está funcionando");
	        }
		 
	 }

}
