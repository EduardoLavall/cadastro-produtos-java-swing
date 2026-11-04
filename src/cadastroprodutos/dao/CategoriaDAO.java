package cadastroprodutos.dao;

import cadastroprodutos.conexao.Conexao;
import cadastroprodutos.beans.Categoria;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/** Consultas parametrizadas para cadastrar, listar, editar e remover categorias. */
public final class CategoriaDAO {
    public List<Categoria> listar() throws SQLException {
        List<Categoria> categorias = new ArrayList<Categoria>();
        String sql = "SELECT id, nome FROM Categorias ORDER BY nome, id";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) categorias.add(new Categoria(rs.getInt("id"), rs.getString("nome")));
        }
        return categorias;
    }

    public int inserir(String nome) throws SQLException {
        String sql = "INSERT INTO Categorias(nome) VALUES (?)";
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, nome);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) return keys.getInt(1);
            }
        }
        throw new SQLException("Categoria inserida, mas nenhum ID foi retornado.");
    }

    public boolean atualizar(int id, String nome) throws SQLException {
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement("UPDATE Categorias SET nome=? WHERE id=?")) {
            ps.setString(1, nome);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    public boolean excluir(int id) throws SQLException {
        try (Connection con = Conexao.abrir();
             PreparedStatement ps = con.prepareStatement("DELETE FROM Categorias WHERE id=?")) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }
}
