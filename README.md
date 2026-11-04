# Cadastro de Produtos e Categorias (Java Swing)

Projeto acadêmico de **Laboratório de Desenvolvimento de Software (UFN)**.
Aplicação desktop feita em **Java Swing + JDBC + MySQL**, organizada para abrir no **Apache NetBeans (Java with Ant)**, conforme o estilo de projetos visto em aula.

## O que o projeto faz

- **Produtos:** cadastrar, listar, editar (inclusive trocar de categoria) e excluir.
- **Categorias:** tela separada para cadastrar, listar, editar e excluir.
- **Pesquisa:** buscar produtos por nome, categoria ou ambos.
- **Relacionamento:** `Produtos.categoria_id` referencia `Categorias.id`.
- **Listagem:** `JTable` apresenta nome, preço, quantidade e categoria.
- **Validações:** campos obrigatórios, preço não negativo e quantidade inteira não negativa.

## Estrutura (padrão das aulas)

```text
cadastro-produtos-java-swing/
├── src/cadastroprodutos/
│   ├── Main.java                      ponto de entrada
│   ├── beans/
│   │   ├── Produto.java               atributos, construtor, getters
│   │   └── Categoria.java
│   ├── conexao/
│   │   └── Conexao.java               JDBC / MySQL
│   ├── dao/
│   │   ├── ProdutoDAO.java            SQL de produtos
│   │   └── CategoriaDAO.java          SQL de categorias
│   ├── telas/
│   │   ├── TelaPrincipal.java         menu e configuração
│   │   ├── TelaProdutos.java          CRUD + JTable + busca
│   │   ├── TelaCategorias.java        cadastro separado
│   │   ├── TelaConfiguracao.java
│   │   └── Avisos.java
│   └── util/Validacao.java            validações de formulário
├── nbproject/                         configuração do NetBeans
├── sql/                               criação do banco e exemplos
├── test/                              testes sem biblioteca adicional
├── lib/                               colocar aqui o MySQL Connector/J
├── build.xml                          comandos do Apache Ant
└── run.bat                             execução via Ant no Windows
```

As telas são construídas diretamente com Swing em arquivos `.java`, como no exemplo de referência do professor. Não usam arquivos `.form` do GUI Builder.

## Como abrir no NetBeans

1. Tenha o **JDK 8 ou superior**, **Apache NetBeans**, **MySQL Server 8+** e **MySQL Connector/J**.
2. Execute `sql/01_criar_banco.sql` no MySQL (Workbench ou linha de comando). Cria o banco `loja_swing` e as tabelas `Categorias` e `Produtos` sem apagar dados.
3. Baixe o driver em <https://dev.mysql.com/downloads/connector/j/> e coloque `mysql-connector-j-*.jar` na pasta `lib/`. Não há senha nem driver empacotado no Git.
4. No NetBeans: **File > Open Project** e escolha esta pasta (contém `build.xml` e `nbproject/`).
5. Use **Run Project**. Se estiver fora do NetBeans, execute `ant run` (ou `run.bat`, com Ant instalado).
6. Em **Configurar conexão**, informe host, porta, banco, usuário e senha do seu MySQL. A senha fica apenas na memória enquanto o programa está aberto.
7. Abra **Categorias**, cadastre uma categoria e então use **Produtos**.

> A conexão usa MySQL local (`localhost:3306`) por padrão. O projeto não inclui credenciais reais; configure com os dados da sua instalação.

## Teste manual completo

1. Cadastre as categorias `Informática` e `Papelaria`.
2. Cadastre um produto `Mouse`, preço `59,90`, quantidade `5`, categoria `Informática`.
3. Verifique a linha na `JTable` e busque por `Mouse`.
4. Edite o produto para preço `49,90` e categoria `Papelaria`; confirme as mudanças na tabela.
5. Busque por categoria `Papelaria` e confirme que `Mouse` aparece.
6. Exclua `Mouse` e confira que sumiu da tabela.
7. Edite o nome de uma categoria e tente excluir uma categoria com produtos: o banco deve impedir enquanto houver vínculo.

## Comandos

```bash
ant clean jar     # compila e monta dist/CadastroProdutosSwing.jar
ant test          # testes de validação e modelos, sem banco
ant run           # executa a interface; requer o driver JDBC em lib/
```

**Verificação já realizada:** compilação com Ant e execução de 28 testes automatizados, sem depender do MySQL. **Pendente:** validar o CRUD ponta a ponta em um MySQL real com Connector/J instalado. Não declare essa etapa como concluída antes de testá-la.

## Observações da disciplina

O exemplo `cadastroEvento_EduardoLavall` foi usado como referência para nomes legíveis, componentes Swing no código, `JTable`, `DefaultTableModel`, estrutura NetBeans/Ant e métodos de ação. Como esta atividade exige relacionamento e persistência no MySQL, a separação `beans` / `dao` / `conexao` segue também a Aula 07. O projeto não replica os problemas de pacote/nome do ZIP de referência.

Leia [`docs/ENTREGA_E_EXPLICACAO.md`](docs/ENTREGA_E_EXPLICACAO.md) para uma explicação resumida dos arquivos e roteiro de apresentação.
