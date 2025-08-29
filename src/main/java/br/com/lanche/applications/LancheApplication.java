package br.com.lanche.applications;

import br.com.lanche.interfaces.LancheRepository;
import br.com.lanche.models.Lanche;
import br.com.lanche.services.LancheService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class LancheApplication {
    private LancheService lancheService;
    private LancheRepository lancheRepository;

    public LancheApplication(LancheRepository lancheRepository, LancheService lancheService) {
        this.lancheService = lancheService;
        this.lancheRepository = lancheRepository;
    }

    public List<Lanche> buscarTodos() throws IOException {
        List<Lanche> lanches = this.lancheRepository.buscarTodos();
        return (lanches != null) ? lanches : new ArrayList<>();
    }


    public Lanche buscarPorId(int id) throws IOException {
        return this.lancheRepository.buscarPorId(id);
    }

    public void adicionar(Lanche lanche) throws IOException {
        this.lancheRepository.adicionar(lanche);
        this.lancheService.salvarImagem(lanche);
    }

    public void excluir(int id) throws IOException {
        this.lancheService.excluirImagem(id);
        this.lancheRepository.excluir(id);
    }

    public void atualizar(int id, Lanche lanche) throws IOException {
        this.lancheRepository.atualizar(id, lanche);
    }

    public double calcularTotal(Lanche lanche, int quantidade) {
        return lanche.calcularLanche(quantidade);
    }
}