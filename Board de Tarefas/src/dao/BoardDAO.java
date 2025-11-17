package dao;

import model.Board;
import model.Card;
import model.Coluna;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class BoardDAO {

    private final ColunaDAO colunaDAO = new ColunaDAO();
    private final CardDAO cardDAO = new CardDAO();

    public Board salvar (Board board) {
        String sql = "INSERT INTO board (nome) VALUES (?)";
        Connection conn = null;

        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, board.getNome());
            int affectedRows = stmt.executeUpdate();

                if (affectedRows > 0) {
                    try (ResultSet rs = stmt.getGeneratedKeys()) {
                        if (rs.next()) {
                            int idGerado = rs.getInt(1);
                            board.setId(idGerado);
                            System.out.println("Board '" + board.getNome() + "' salvo com ID: " + idGerado);

                            if (salvarColunas (conn, board)) {
                                conn.commit();
                                System.out.println("Colunas salvas com sucesso.");
                                return board;
                            } else {
                                conn.rollback();
                                System.out.println("Falhas ao salvar colunas. Transação desfeita");
                                return null;
                            }
                        }
                    }
                }
            }
        } catch (SQLException e){
            System.err.println("Erro geral na transação do board: " + board.getNome());
            e.printStackTrace();
        try {
            if (conn != null) conn.rollback();
        } catch (SQLException rollbackEx) {
            System.out.println("Erro ao tentar rollback.");
            rollbackEx.printStackTrace();
        }
        return null;
        } finally {
            try {
                if (conn != null) conn.close();
            } catch (SQLException closeEx) {
                System.out.println("Erro ao fechar conexão.");
                closeEx.printStackTrace();
            }
        }
        return null;
    }

    private boolean salvarColunas (Connection conn, Board board){
        ColunaDAO colunaDAO = new ColunaDAO();

        for(Coluna coluna : board.getColunas()){
            coluna.setIdBoard(board.getId());

            Coluna colunaSalva = colunaDAO.salvar(conn,coluna);

            if (colunaSalva == null){
                return false;
            }
        }
        return true;
    }

    public List<Board> buscarTodos (){
        String sql = "SELECT id, nome FROM board ORDER BY nome";
        List<Board> boards = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                int id = rs.getInt("id");
                String nome = rs.getString("nome");

                Board board = new Board(nome);
                board.setId(id);

                boards.add(board);
            }
        } catch (SQLException e) {
            System.err.println("Erro ao buscar todos os boards.");
            e.printStackTrace();
        }
        return boards;
    }

    public Board carregarColunas (Board board){
        if (board == null || board.getId() == 0) {
            System.err.println("Erro: Não é possível carregar colunas para um board sem ID");
            return board;
        }

        board.setColunas(colunaDAO.buscaPorBoardId(board.getId()));

        List<Card> todosOsCards = cardDAO.buscarCardsPorBoardID(board.getId());
        for (Coluna coluna : board.getColunas()) {
            List<Card> cardsNaColuna = new ArrayList<>();

            for (Card card : todosOsCards) {
                if (card.getIdColunaAtual() == coluna.getId()) {
                    cardsNaColuna.add(card);
                }
            }
            coluna.setCards(cardsNaColuna);
        }

        return board;
    }

    public boolean excluir(int idBoard) {
        if (cardDAO.boardTemCards(idBoard)) {
            System.err.println("Erro: O Board ID " + idBoard + " não pode ser deletado pois ainda possui cards");
            return false;
        }

        String sqlExcluirColunas = "DELETE FROM coluna WHERE id_board = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtColunas = conn.prepareStatement(sqlExcluirColunas)) {

            stmtColunas.setInt(1, idBoard);
            stmtColunas.executeUpdate();

            System.out.println("Colunas do Board ID " + idBoard + " deletadas com sucesso.");
        } catch (SQLException e) {
            System.err.println("Erro ao excluir as colunas do Board ID: " + idBoard);
            return false;
        }

        String sqlExcluirBoard = "DELETE FROM board WHERE id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmtExcluirBoard = conn.prepareStatement(sqlExcluirBoard)) {

            stmtExcluirBoard.setInt(1, idBoard);
            int affectedRows = stmtExcluirBoard.executeUpdate();

            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Erro ao excluir o Board ID: " + idBoard);
            e.printStackTrace();
            return false;
        }
    }
}
