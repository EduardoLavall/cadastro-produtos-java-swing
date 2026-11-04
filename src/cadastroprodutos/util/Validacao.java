package cadastroprodutos.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public final class Validacao {
    private Validacao() { }

    public static String nomeProduto(String nome) {
        return nomeObrigatorio(nome, 100, "Nome do produto");
    }
    public static String nomeCategoria(String nome) {
        return nomeObrigatorio(nome, 80, "Nome da categoria");
    }
    private static String nomeObrigatorio(String texto, int limite, String descricao) {
        String valor = texto == null ? "" : texto.trim();
        if (valor.isEmpty()) throw new IllegalArgumentException(descricao + " é obrigatório.");
        if (valor.length() > limite) throw new IllegalArgumentException(
            descricao + " deve ter no máximo " + limite + " caracteres.");
        return valor;
    }

    public static BigDecimal preco(String texto) {
        if (texto == null || texto.trim().isEmpty())
            throw new IllegalArgumentException("Informe o preço do produto.");
        String valor = texto.trim().replace(',', '.');
        try {
            BigDecimal preco = new BigDecimal(valor);
            if (preco.signum() < 0)
                throw new IllegalArgumentException("O preço não pode ser negativo.");
            if (preco.scale() > 2)
                throw new IllegalArgumentException("O preço aceita até duas casas decimais.");
            preco = preco.setScale(2, RoundingMode.UNNECESSARY);
            if (preco.compareTo(new BigDecimal("99999999.99")) > 0)
                throw new IllegalArgumentException("Preço acima do limite de 99.999.999,99.");
            return preco;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Preço inválido. Use 12,50 ou 12.50.");
        }
    }

    public static int quantidade(String texto) {
        if (texto == null || texto.trim().isEmpty())
            throw new IllegalArgumentException("Informe a quantidade em estoque.");
        try {
            int quantidade = Integer.parseInt(texto.trim());
            if (quantidade < 0) throw new IllegalArgumentException("Quantidade não pode ser negativa.");
            return quantidade;
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Quantidade inválida. Digite um número inteiro.");
        }
    }

    public static int categoriaSelecionada(int id) {
        if (id <= 0) throw new IllegalArgumentException("Cadastre e selecione uma categoria.");
        return id;
    }
}
