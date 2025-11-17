package app;

import dao.BoardDAO;
import dao.CardDAO;
import model.Board;
import model.Card;
import model.Coluna;
import model.TipoColuna;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MenuPrincipal {

    private final Scanner scanner;
    private final BoardDAO boardDAO;
    private Board boardSelecionado;

    public MenuPrincipal() {
        this.scanner = new Scanner(System.in);
        this.boardDAO = new BoardDAO();
    }

    public static void main(String[] args) {
        MenuPrincipal app = new MenuPrincipal();
        app.iniciar();
    }

    public void iniciar() {
        int opcao;
        do {
            exibirMenuPrincipal();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    criarNovoBoard();
                    break;
                case 2:
                    selecionarBoard();
                    break;
                case 3:
                    excluirBoard();
                    break;
                case 4:
                    System.out.println("Saindo do sistema!");
                    break;
                default:
                    System.out.println("Opção inválida, por favor seleciona uma das opções listadas acima");
            }
        } while (opcao != 4);

        scanner.close();
    }

    public void exibirMenuPrincipal() {
        System.out.println("------- Board de Tarefas -------");
        System.out.println("1 - Criar novo board");
        System.out.println("2 - Selecionar board");
        System.out.println("3 - Excluir board");
        System.out.println("4 - Sair");
        System.out.println("Escolha uma opcao: ");
    }

    public int lerOpcao() {
        try {
            String linha = scanner.nextLine();
            return Integer.parseInt(linha);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void criarNovoBoard() {
        System.out.println("--- Criar novo board ----");
        System.out.println("Informe o nome do seu novo board: ");
        var nomeBoard = scanner.nextLine();

        Board novoBoard = new Board(nomeBoard);
        List<Coluna> colunas = new ArrayList<>();
        colunas.add(new Coluna("A fazer", 1, TipoColuna.INICIAL));
        colunas.add(new Coluna("Em Progresso", 1, TipoColuna.PENDENTE));
        colunas.add(new Coluna("Concluído", 1, TipoColuna.FINAL));
        colunas.add(new Coluna("Cancelados", 1, TipoColuna.CANCELAMENTO));

        colunas.forEach(novoBoard::adicionarColuna);

        if (novoBoard.validarEstrutura()) {
            Board boardSalvo = boardDAO.salvar(novoBoard);
            if (boardSalvo != null) {
                System.out.println("Board " + novoBoard.getNome() + " criado com sucesso!");
            } else {
                System.out.println("Falha ao criar novo board. Verifique o log de erros.");
            }
        } else {
            System.out.println("Erro: A estrutura de colunas é inválida. O board não foi criado");
            }
    } // Opção 1  do Menu Principal

    private void selecionarBoard() {
        List<Board> boards = boardDAO.buscarTodos();


        if (boards.isEmpty()) {
            System.out.println("Não foram encontrados boards para seleção.");
            return;
        }

        exibirListaDeBoards(boards);
        System.out.println("Deseje o ID do board que deseja selecionar:");
        System.out.println("Digite  0 para voltar ao Menu Principal");
        int escolhaId = lerOpcao();

        if (escolhaId == 0) {
            return;
        }

        Board boardSelecionadoPorId = boards.stream()
                                            .filter(b -> b.getId() == escolhaId)
                                            .findFirst()
                                            .orElse(null);

        if (boardSelecionadoPorId != null) {
            this.boardSelecionado = boardDAO.carregarColunas(boardSelecionadoPorId);

            if (this.boardSelecionado != null) {
                System.out.println("\n Board " + this.boardSelecionado.getNome() + " selecionado com sucesso!");
                menuManipulacaoBoard(this.boardSelecionado);
            } else {
                System.out.println("Erro ao carregar o board completo");
            }
        } else {
            System.out.println("Opção de board inválida");
        }
    } // Opção 2 do Menu Principal

    private void excluirBoard() {
        System.out.println("--- Excluir Board ---");

        List<Board> boards = boardDAO.buscarTodos();

        if (boards.isEmpty()) {
            System.out.println("Nenhum board encontrado para exclusão.");
            return;
        }

        exibirListaDeBoards(boards);
        System.out.println("Informe o Board ID que deseja excluir: ");
        int escolhaId = lerOpcao();

        if (escolhaId == 0) {
            return;
        }

        Board boardParaExcluir = boards.stream()
                                        .filter(b -> b.getId() == escolhaId)
                                        .findFirst()
                                        .orElse(null);

        if (boardParaExcluir != null) {
            System.out.println("Confirma que deseja excluir o Board " + boardParaExcluir.getNome() + " (ID:" + boardParaExcluir.getId() + ")? S/N");
            String confirmacao = scanner.nextLine().trim().toUpperCase();

            if (confirmacao.equals("S")) {
                if (boardDAO.excluir(boardParaExcluir.getId())) {
                    System.out.println("Board excluido com sucesso!");
                } else {
                    System.out.println("Falha na exclusão do board. Verifique se ele está vazio.");
                }
            } else {
                System.out.println("Exclusão cancelada.");
            }
        } else {
            System.out.println("ID de board inválida ou não encontrado.");
        }
    } // Opção 3 do Menu Principal

    public void exibirListaDeBoards(List<Board> boards) {
        if (boards.isEmpty()) {
            System.out.println("Nenhum board foi encontrado. Crie um board antes de tentar selecionar.");
            return;
        }
        System.out.println("--- Lista de boards ---");
        for (Board board : boards) {
            System.out.println(board.getNome() + " (ID: " + board.getId() + ")");
        }
    } //Metodo auxiliar para mostrar os Boards que existem por seu ID

    private void exibirMenuManipulacao(Board board) {
        System.out.println("--- Board: " + board.getNome() + " (ID: " + board.getId() + ") ---");
        System.out.println("1 - Visualizar board");
        System.out.println("2 - Criar novo card");
        System.out.println("3 - Mover card");
        System.out.println("4 - Bloquear Card");
        System.out.println("5 - Desbloquear Card");
        System.out.println("6 - Voltar ao Menu Principal");
        System.out.println("Escolha uma das opções acima:");
    } //Lista das opções disponíveis após selecionar o board

    private void menuManipulacaoBoard(Board board) {
        int opcao;
        do {
            exibirMenuManipulacao(board);
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    visualizarBoard(board);
                    break;
                case 2:
                    criarNovoCard(board);
                    break;
                case 3:
                    moverCard(board);
                    break;
                case 4:
                    bloquearCard(board);
                    break;
                case 5:
                    desbloquearCard(board);
                    break;
                case 6:
                    System.out.println("\n Retornando ao Menu Principal");
                default:
                    System.out.println("Opção inválida. Por gentileza, selecione uma opção válida.");
            }
        } while (opcao != 6);
    } // Menu (switch) das opções disponíveis após selecionar o board

    private void visualizarBoard(Board board) {
        System.out.println("--- Confira como está o Board " +board.getNome() + " neste momento:");

        for (Coluna coluna : board.getColunas()) {
            System.out.println(" " + coluna.getNome() + " - Cards: " + coluna.getCards().size());

            if (!coluna.getCards().isEmpty()) {
                for (Card card : coluna.getCards()) {
                    String status = card.isBloqueado() ? "(Bloqueado)" : "";
                    System.out.println("  - Card: " + card.getTitulo() + ", ID: " + card.getId() + " " + status);
                }
            }
        }
    } // Opção 1 do submenu do Board

    private void visualizarBoardParaEdicao(Board board) {
        System.out.println("--- Confira como está o Board " +board.getNome() + " neste momento:");

        for (Coluna coluna : board.getColunas()) {
            System.out.println(" " + coluna.getNome() + " (Tipo: " + coluna.getTipo() + ", ID: " + coluna.getId() + " - Cards: " + coluna.getCards().size());

            if (!coluna.getCards().isEmpty()) {
                for (Card card : coluna.getCards()) {
                    String status = card.isBloqueado() ? "(Bloqueado)" : "";
                    System.out.println("  - Card: " + card.getTitulo() + ", ID: " + card.getId() + " " + status);
                }
            }
        }
    } // Opção 1 do submenu do Board

    private void criarNovoCard(Board board) {
        System.out.println("--- Criar novo card para o Board: " +  board.getNome() + " ---");

        Coluna colunaInicial = null;
        for (Coluna coluna : board.getColunas()) {
            if (coluna.getTipo() == TipoColuna.INICIAL) {
                colunaInicial = coluna;
                break;
            }
        }
        if (colunaInicial == null) {
            System.out.println("Erro: O Board não possui uma coluna do tipo INICIAL. Necessário verificar.");
            return;
        }

        System.out.println("Digite o Título do Card:");
        var titulo = scanner.nextLine();
        System.out.println("Digite a Descrição do Card:");
        var descricao = scanner.nextLine();

        Card novoCard = new Card(titulo, descricao);

        novoCard.setIdColunaAtual(colunaInicial.getId());

        CardDAO cardDAO = new CardDAO();
        Card cardSalvo = cardDAO.salvar(novoCard);

        if (cardSalvo != null) {
            colunaInicial.adicionarCard(cardSalvo);
            System.out.println("Card " + cardSalvo.getTitulo() + " criado com sucesso!");
        } else {
            System.out.println("Falha ao salvar o Card no Banco de Dados.");
        }
    } // Opção 2 do submenu do Board

    private void moverCard(Board board) {
        System.out.println("--- Mover Card ---");

        visualizarBoardParaEdicao(board);

        List<Card> todosOsCards = new ArrayList<>();
        board.getColunas().forEach(col -> todosOsCards.addAll(col.getCards()));
        List <Coluna> colunas = board.getColunas();

        if (todosOsCards.isEmpty()) {
            System.out.println("Nenhum card para mover neste board");
            return;
        }

        System.out.println("Digite o ID do Card que deseja mover:");
        int cardID = lerOpcao();

        Card cardParaMover = todosOsCards.stream()
                                        .filter( c -> c.getId() == cardID)
                                        .findFirst()
                                        .orElse(null);

        if (cardParaMover == null) {
            System.out.println("Card com ID " + cardID + " não encontrado.");
            return;
        }

        if (cardParaMover.isBloqueado()) {
            System.out.println("Não é possível mover um card BLOQUEADO.");
            return;
        }

        Coluna colunaOrigem = board.getColunas().stream()
                                                .filter(col -> col.getId() == cardParaMover.getIdColunaAtual())
                                                .findFirst()
                                                .orElse(null);


        if (colunaOrigem == null) {
            System.out.println("Erro: Coluna de Origem nao encontrada.");
            return;
        }

        System.out.println("\n--- Colunas Disponíveis ---");
        board.getColunas().forEach(col ->
                System.out.println("ID: " + col.getId() + " - " + col.getNome())
        );

        System.out.println("Para qual ID de Coluna você deseja mover o card " + cardID + " (" + cardParaMover.getTitulo() + ")? ");
        int novaColunaId = lerOpcao();

        Coluna colunaDestino = board.getColunas().stream()
                                                .filter(col -> col.getId() == novaColunaId)
                                                .findFirst()
                                                .orElse(null);

        if (colunaDestino == null) {
            System.out.println("Coluna com ID " + novaColunaId + " não encontrada ou inválida");
            return;
        }

        if (colunaOrigem.getId() == colunaDestino.getId()) {
            System.out.println("O Card já está na Coluna desejada.");
            return;
        }

        int indexOrigem = colunas.indexOf(colunaOrigem);
        int indexDestino = colunas.indexOf(colunaDestino);

        int indexColunaCancelados = colunas.size() - 1;
        int indexColunaConcluidos = colunas.size() - 2;

        boolean movimentacaoValida = false;

        if (indexDestino == indexColunaCancelados) {
            if (indexOrigem != indexColunaConcluidos) {
                movimentacaoValida = true;
            }
        }
        else {
            if (indexDestino == indexOrigem + 1) {
                movimentacaoValida = true;
            }
        }

        if (!movimentacaoValida) {
            String motivo = (indexDestino == indexColunaCancelados) ?
                    "Não é permitido mover um card da Coluna '" + colunas.get(indexColunaConcluidos).getNome() + "' para a coluna de Cancelados." :
                    "O Card deve ser movido sequencialmente para a próxima coluna, sem pular etapas.";

            System.out.println("Erro: " + motivo);
            return;
        }

        colunaOrigem.removerCard(cardParaMover);
        colunaDestino.adicionarCard(cardParaMover);

        cardParaMover.setIdColunaAtual(colunaDestino.getId());

        CardDAO cardDAO = new CardDAO();
        if (cardDAO.atualizar(cardParaMover)) {
            System.out.println("Card movido com sucesso de " + colunaOrigem.getNome() + " para " + colunaDestino.getNome() + ".");
        } else {

            cardParaMover.setIdColunaAtual(colunaOrigem.getId());
            colunaDestino.removerCard(cardParaMover);
            colunaOrigem.adicionarCard(cardParaMover);

            System.out.println("Erro: Movimento falhou ao atualizar o banco de dados. O Card não foi movido e retornou a coluna de origem.");
        }
    } // Opção 3 do submenu do Board

    private void bloquearCard (Board board) {
        System.out.println("\n--- Bloquear Card ---");

        Card card = selecionarCardPorId(board);
        if (card == null) return;

        if (card.isBloqueado()) {
            System.out.println("O Card ID " + card.getId() + " já está bloqueado.");
            return;
        }

        System.out.println("Digite o motivo do bloqueio: ");
        String motivo = scanner.nextLine();

        boolean sucesso = card.bloquear(motivo);

        if (sucesso) {
            CardDAO cardDAO = new CardDAO();
            if (cardDAO.atualizar(card)) {
                System.out.println("Card ID " + card.getId() + " bloqueado com sucesso!");
            } else {
                System.out.println("Bloqueio realizado na memória, mas falhou ao salvar no banco de dados.");
            }
        }

    } // Opção 4 do submenu do Board

    private void desbloquearCard (Board board) {
        System.out.println("\n--- Desbloquear Card ---");

        Card card = selecionarCardPorId(board);
        if (card == null) return;

        if (!card.isBloqueado()) {
            System.out.println("O Card ID " + card.getId() + " não está desbloqueado.");
            return;
        }

        System.out.println("Digite o motivo do bloqueio: ");
        String motivo = scanner.nextLine();

        boolean sucesso = card.desbloquear(motivo);

        if (sucesso) {
            CardDAO cardDAO = new CardDAO();
            if (cardDAO.atualizar(card)) {
                System.out.println("Card ID " + card.getId() + " desbloqueado com sucesso!");
            } else {
                System.out.println("Desbloqueio realizado na memória, mas falhou ao salvar no banco de dados.");
            }
        }
    }  // Opção 5 do submenu do Board

    private Card selecionarCardPorId(Board board) {
        visualizarBoardParaEdicao(board);

        List<Card> todosOsCards = new ArrayList<>();
        board.getColunas().forEach(col -> todosOsCards.addAll(col.getCards()));

        if (todosOsCards.isEmpty()) {
            System.out.println("Nenhum card encontrado neste board");
            return null;
        }

        System.out.println("Digite o ID do Card que deseja selecionar:");
        int cardID = lerOpcao();

        Card card = todosOsCards.stream()
                .filter(c -> c.getId() == cardID)
                .findFirst()
                .orElse(null);
        if (card == null) {
            System.out.println("O Card ID " + cardID + " não foi encontrado.");
        }
        return card;
    }
}
