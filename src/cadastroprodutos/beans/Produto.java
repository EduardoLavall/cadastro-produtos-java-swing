package cadastroprodutos.beans;

import java.math.BigDecimal;

public final class Produto {
    private final int id;
    private final String nome;
    private final BigDecimal preco;
    private final int quantidade;
    private final int categoriaId;
    private final String categoriaNome;

    public Produto(int id, String nome, BigDecimal preco, int quantidade,
                   int categoriaId, String categoriaNome) {
        this.id = id;
        this.nome = nome;
        this.preco = preco;
        this.quantidade = quantidade;
        this.categoriaId = categoriaId;
        this.categoriaNome = categoriaNome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }
    public BigDecimal getPreco() { return preco; }
    public int getQuantidade() { return quantidade; }
    public int getCategoriaId() { return categoriaId; }
    public String getCategoriaNome() { return categoriaNome; }
}
