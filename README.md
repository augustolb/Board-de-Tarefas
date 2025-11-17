📋 Gerenciador de Board de Tarefas (Kanban Simples)

Projeto desenvolvido em Java para simular um sistema simplificado de gestão de tarefas Kanban, utilizando um banco de dados MySQL para persistência dos dados. O objetivo principal foi aplicar conceitos de Programação Orientada a Objetos (POO), padrão DAO (Data Access Object) e regras de negócio específicas.

✨ Funcionalidades:

O sistema permite a gestão completa de Boards, Colunas e Cards, obedecendo a regras de negócio essenciais para o fluxo de trabalho Kanban:
- Gestão de Boards: Criação, seleção e exclusão de Boards.
- Criação de Cards: Adição de novas tarefas com título, descrição, e prazo.
- Movimentação Controlada de Cards:
  - Um Card deve ser movido sequencialmente (sem pular colunas), com a regrade de que os Cards podem ser movidos para a coluna "Cancelados" a partir de qualquer coluna, exceto da coluna final (Concluído ).
- Bloqueio/Desbloqueio de Cards: Permite bloquear um Card, impedindo sua movimentação, e registrar o motivo do bloqueio e desbloqueio.
- Regra de Exclusão: Um Board só pode ser excluído se estiver completamente vazio (não contiver Cards).


🏛️ Arquitetura do Projeto

O projeto utiliza uma arquitetura baseada em camadas para garantir a separação de responsabilidades:

  - app.MenuPrincipal: Camada de interface de usuário (CLI).

  - model: Contém as classes de modelo de dados (ex: Board, Coluna, Card).

  - dao (Data Access Object): Gerencia toda a comunicação com o banco de dados (BoardDAO, ColunaDAO, CardDAO).

  - connection.DBConnection: Classe responsável por estabelecer e gerenciar a conexão com o MySQL.
