package dao;

import model.Card;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class CardDAO {

    public Card salvar(Card card) {
        String sql = "INSERT INTO card (id_coluna, titulo, descricao, data_criacao, bloqueado ) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn =DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, card.getIdColunaAtual());
            stmt.setString(2, card.getTitulo());
            stmt.setString(3, card.getDescricao());
            stmt.setTimestamp(4, Timestamp.valueOf(card.getDataCriacao()));
            stmt.setBoolean(5, card.isBloqueado());

            int affectedRows = stmt.executeUpdate();

            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()){
                        card.setId(rs.getInt(1));
                        return card;
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao salvar o card: " + card.getTitulo());
            e.printStackTrace();
        }
        return null;
    }

    public List<Card> buscarCardsPorBoardID(int idBoard) {
        String sql = "SELECT c.id, c.id_coluna, c.titulo, c.descricao, c.data_criacao, c.bloqueado, c.motivo_bloqueio, c.motivo_desbloqueio " +
                "FROM card c " +
                "JOIN coluna col ON c.id_coluna = col.id " +
                "WHERE col.id_board = ?";

        List<Card> cards = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, idBoard);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Card card = new Card(
                            rs.getString("titulo"),
                            rs.getString("descricao")
                    );

                    card.setId(rs.getInt("id"));
                    card.setIdColunaAtual(rs.getInt("id_coluna"));

                    card.setDataCriacao(rs.getTimestamp("data_criacao").toLocalDateTime());

                    if (rs.getBoolean("bloqueado")) {
                        card.bloquear(rs.getString("motivo_bloqueio"));
                    } else {
                        card.desbloquear(rs.getString("motivo_desbloqueio"));
                    }
                    cards.add(card);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar cards para o Board ID: " + idBoard);
            e.printStackTrace();
        }
        return cards;
    }

    public boolean atualizar (Card card) {
        String sql = "UPDATE card SET id_coluna = ?, bloqueado = ?, motivo_bloqueio = ?, motivo_desbloqueio = ? WHERE id = ? ";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, card.getIdColunaAtual());
            stmt.setBoolean(2, card.isBloqueado());
            stmt.setString(3, card.getMotivoBloqueio());
            stmt.setString(4, card.getMotivoDesbloqueio());
            stmt.setInt(5, card.getId());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;

        } catch (SQLException e) {
            System.err.println("Erro ao atualizar card ID: " + card.getId());
            e.printStackTrace();
            return false;
        }
    }

    public boolean boardTemCards (int idBoard) {
        String sql = "SELECT COUNT(c.id) FROM card c JOIN coluna col ON c.id_coluna = col.id WHERE col.id_board = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1,idBoard);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            System.err.println("Erro ao verificar se o Board ID: " + idBoard + " tem cards.");
            e.printStackTrace();
            return true;
        }
        return false;
    }
}
