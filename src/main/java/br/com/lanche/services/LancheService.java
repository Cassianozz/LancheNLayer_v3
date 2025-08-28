package br.com.lanche.services;

import br.com.lanche.interfaces.LancheRepository;
import br.com.lanche.models.Lanche;
import br.com.lanche.repositories.LancheRepositoryImpl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class LancheService {
    private static final String PASTA_IMAGENS = "produto_imagens/";
    private final LancheRepository repository;

    public LancheService(LancheRepository repository) {
        this.repository = repository;
    }

    public String salvarImagem(Lanche lanche) throws IOException, IOException {
        String caminhoImagem = lanche.getCaminhoImagem();

        Files.createDirectories(Paths.get(PASTA_IMAGENS));

        String nomeArquivo = Paths.get(caminhoImagem).getFileName().toString();

        Path destino = Paths.get(PASTA_IMAGENS + nomeArquivo);

        Files.move(Paths.get(caminhoImagem), destino, StandardCopyOption.REPLACE_EXISTING);

        return destino.toString();
    }
    public String moverImagem(Lanche lanche, String novaPastaDestino) throws IOException {
        Path origem = Paths.get(lanche.getCaminhoImagem()); // precisa ser um arquivo!
        if (!Files.exists(origem) || Files.isDirectory(origem)) {
            throw new IOException("O caminho de origem não é um arquivo válido: " + origem);
        }

        // cria a pasta destino se não existir
        Path pastaDestino = Paths.get(novaPastaDestino);
        Files.createDirectories(pastaDestino);

        // mantém o mesmo nome do arquivo
        Path destino = pastaDestino.resolve(origem.getFileName());

        // move o arquivo
        Files.move(origem, destino, StandardCopyOption.REPLACE_EXISTING);

        return destino.toString();
    }
    public void excluirImagem(int id) throws IOException {
        Lanche lanche = repository.buscarPorId(id);

        if (lanche == null) {
            throw new IOException("Lanche com ID " + id + " não encontrado.");
        }

        Path caminho = Paths.get(lanche.getCaminhoImagem());
        if (Files.exists(caminho) && !Files.isDirectory(caminho)) {
            Files.delete(caminho);
        } else {
            throw new IOException("Arquivo de imagem não encontrado: " + caminho);
        }
    }
    }

