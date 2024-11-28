package br.com.ucsal.controller;

import br.com.ucsal.service.ProdutoService;
import br.com.ucsal.persistencia.ContainerInjector;

public abstract class NewBaseRotas {
    protected final ProdutoService produtoService;

    public NewBaseRotas() {
        ProdutoService service = new ProdutoService();
        ContainerInjector.injetarDependencias(service);
        this.produtoService = service;
    }
}
