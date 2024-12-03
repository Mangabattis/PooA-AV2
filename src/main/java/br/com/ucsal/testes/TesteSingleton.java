package br.com.ucsal.testes;

import br.com.ucsal.persistencia.MemoriaProdutoRepository;
import br.com.ucsal.persistencia.SingletonMemoria;

public class TesteSingleton {
    public static void main(String[] args) {
        try {
            MemoriaProdutoRepository repo1 = SingletonMemoria.getInstance(MemoriaProdutoRepository.class);
            System.out.println("Instância 1: " + repo1);

            MemoriaProdutoRepository repo2 = SingletonMemoria.getInstance(MemoriaProdutoRepository.class);
            System.out.println("Instância 2: " + repo2);

            if (repo1 == repo2) {
                System.out.println("O Singleton está funcionando corretamente: as instâncias são as mesmas.");
            }

        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        }
    }
}
