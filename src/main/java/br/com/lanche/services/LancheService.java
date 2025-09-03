package br.com.lanche.services;

import br.com.lanche.interfaces.LancheRepository;
import br.com.lanche.models.Lanche;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class LancheService {
    private static final String PASTA_IMAGENS = "produto_imagens/";
    private final LancheRepository repository;

    public LancheService(LancheRepository repository) {
        this.repository = repository;
    }

    public String salvarImagem(Lanche lanche) throws IOException {
        String caminhoImagem = lanche.getCaminhoImagem();
        Path origem = Paths.get(caminhoImagem);

        if (!Files.exists(origem) || Files.isDirectory(origem)) {
            throw new IOException("Arquivo de imagem inválido: " + caminhoImagem);
        }

        Files.createDirectories(Paths.get(PASTA_IMAGENS));

        Path destino = Paths.get(PASTA_IMAGENS, origem.getFileName().toString());

        Files.copy(origem, destino, StandardCopyOption.REPLACE_EXISTING);

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
        Path pastaImagens = Paths.get(PASTA_IMAGENS);

        if (!Files.exists(pastaImagens) || !Files.isDirectory(pastaImagens)) {
            throw new IOException("Pasta de imagens não encontrada.");
        }

        try (Stream<Path> imagens = Files.list(pastaImagens)) {
            boolean encontrado = imagens
                    .filter(Files::isRegularFile)
                    .filter(imagem -> imagem.getFileName().toString().contains(String.valueOf(id)))
                    .map(imagem -> {
                        try {
                            Files.delete(imagem);
                            return imagem;
                        } catch (IOException e) {
                            throw new RuntimeException("Erro ao excluir a imagem: " + imagem, e);
                        }
                    })
                    .findFirst()
                    .isPresent();

            if (!encontrado) {
                throw new IOException("Nenhuma imagem encontrada para o ID: " + id);
            }
        }
    }
}