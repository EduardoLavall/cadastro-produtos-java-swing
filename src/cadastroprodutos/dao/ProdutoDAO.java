package cadastroprodutos.dao;

import cadastroprodutos.conexao.Conexao;
import cadastroprodutos.beans.Produto;
import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** CRUD de produtos + busca por nome/categoria usando INNER JOIN. */
public final class ProdutoDAO {
    public List<Produto> buscar(String texto, String campo) throws SQLException {
        List<Produto> produtos = new ArrayList<Produto>();
        String base = "SELECT p.id, p.Nome, p.Preco, p.Quantidade, "
            + "p.categoria_id, c.nome AS categoria_nome FROM Produtos p "
            + "INNER JOIN Categorias c ON c.id=p.categoria_id";
        boolean filtro = texto != null && !texto.trim().isEmpty();
        if (filtro) {
            if ("Nome".equals(campo)) base += " WHERE LOWER(p.Nome) LIKE ?";
            else if ("Categoria".equals(campo)) base += " WHERE LOWER(c.nome) LIKE ?";
            else base += " WHERE (LOWER(p.Nome) LIKE ? OR LOWER(c.nome) LIKE ?)";
        }
        base += " ORDER BY p.Nome, p.id";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(base)) {
            if (filtro) {
                String busca = "%" + texto.trim().toLowerCase(java.util.Locale.ROOT) + "%";
                ps.setString(1, busca);
                if (!"Nome".equals(campo) && !"Categoria".equals(campo)) ps.setString(2, busca);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    produtos.add(new Produto(rs.getInt("id"), rs.getString("Nome"),
                        rs.getBigDecimal("Preco"), rs.getInt("Quantidade"),
                        rs.getInt("categoria_id"), rs.getString("categoria_nome")));
                }
            }
        }
        return produtos;
    }

    public int inserir(String nome, BigDecimal preco, int quantidade, int categoriaId) throws SQLException {
        String sql = "INSERT INTO Produtos(Nome, Preco, Quantidade, categoria_id) VALUES (?, ?, ?, ?)";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.setBigDecimal(2, preco);
            ps.setInt(3, quantidade);
            ps.setInt(4, categoriaId);
            ps.executeUpdate();
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
        }
        throw new SQLException("Produto inserido, mas nenhum ID foi retornado.");
    }

    public boolean atualizar(int id, String nome, BigDecimal preco, int quantidade,
                             int categoriaId) throws SQLException {
        String sql = "UPDATE Produtos SET Nome=?, Preco=?, Quantidade=?, categoria_id=? WHERE id=?";
        try (Connection con = Conexao.abrir(); PreparedStatement ps=con.prepareStatement(sql)) {
            ps.setString(1, nome);
            ps.setBigDecimal(2, preco);
            ps.setInt(3, quantidade);
            ps.setInt(4, categoriaId);
            ps.setInt(5, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection con=Conexao.abrir();
             PreparedStatement ps=con.prepareStatement("DELETE FROM Produtos WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
