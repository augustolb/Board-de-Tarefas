package dao;

import model.Coluna;
import model.TipoColuna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ColunaDAO {

    public Coluna salvar(Connection conn, Coluna coluna) {
        String sql = "INSERT INTO coluna (id_board, nome, ordem, tipo) values (?, ?, ?, ?)";

        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, coluna.getIdBoard());
            stmt.setString(2, coluna.getNome());
            stmt.setInt(3, coluna.getOrdem());
            stmt.setString(4, coluna.getTipo().name());

            int affectRows = stmt.executeUpdate();

            if (affectRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        int idGerado = rs.getInt(1);
                        coluna.setId(idGerado);
                        return coluna;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar a coluna: " + coluna.getNome() + " para o Board ID:" + coluna.getIdBoard());
            e.printStackTrace();
        }
        return null;
    }

    public List<Coluna> buscaPorBoardId(int idBoard) {
        String sql = "SELECT id, nome, ordem, tipo, id_board FROM coluna WHERE id_board = ? ORDER BY ordem";
        List<Coluna> colunas = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, idBoard);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String nome = rs.getString("nome");
                    int ordem = rs.getInt("ordem");

                    TipoColuna tipo = TipoColuna.valueOf(rs.getString("tipo"));

                    Coluna coluna = new Coluna(nome, ordem, tipo);
                    coluna.setId(rs.getInt("id"));
                    coluna.setIdBoard(rs.getInt("id_board"));

                    colunas.add(coluna);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar colunas para o Board ID: " + idBoard);
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.err.println("Erro de mapeamento de TipoColuna: " + e.getMessage());
        }
        return colunas;
    }
}
