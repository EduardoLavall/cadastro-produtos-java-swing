# Guia da entrega e explicação para o professor

## Requisitos atendidos

| Requisito do enunciado | Arquivo |
| --- | --- |
| Tabelas Categorias e Produtos, chave estrangeira | `sql/01_criar_banco.sql` |
| Cadastrar e alterar produtos com categoria | `telas/TelaProdutos.java`, `dao/ProdutoDAO.java` |
| Listagem de produtos e estoque | `telas/TelaProdutos.java` + `JTable` |
| Buscar produto por nome ou categoria | `dao/ProdutoDAO.java` (`INNER JOIN` e `LIKE`) |
| Tela separada para categorias | `telas/TelaCategorias.java` |
| CRUD completo de produtos | `dao/ProdutoDAO.java`, `telas/TelaProdutos.java` |

## Explicação rápida (código)

- `Main.java`: abre a janela inicial usando `SwingUtilities.invokeLater`.
- `beans/`: classes de dados com atributos e getters.
- `conexao/Conexao.java`: faz a conexão JDBC com MySQL; configurações temporárias, sem senha no Git.
- `dao/`: realiza `INSERT`, `SELECT`, `UPDATE` e `DELETE` com `PreparedStatement`.
- `telas/`: componentes `JFrame`, `JTextField`, `JComboBox`, `JButton`, `DefaultTableModel`, `JTable` e eventos de clique.
- `util/Validacao.java`: verifica os valores informados antes de enviar para o banco.

## Demonstração em 2 minutos

1. Abrir o menu e testar conexão MySQL.
2. Abrir Categorias e cadastrar `Papelaria`.
3. Abrir Produtos e cadastrar `Caneta`, `2,50`, estoque `20`, categoria `Papelaria`.
4. Mostrar a linha na `JTable`; pesquisar por nome e categoria.
5. Editar a categoria e salvar; mostrar resultado na tabela.
6. Excluir o produto e conferir a remoção.

## Antes de enviar

- [ ] Abrir o projeto no NetBeans.
- [ ] Executar o SQL de criação do banco.
- [ ] Instalar o MySQL Connector/J em `lib/`.
- [ ] Testar os fluxos reais de CRUD e busca (não foi possível testar com MySQL neste ambiente).
- [ ] Confirmar forma de entrega exigida pelo professor (link GitHub ou ZIP).

Não inclua senhas, arquivos `.env`, `build/`, `dist/` ou driver JDBC no repositório público.
