package cadastroprodutos.telas;

import cadastroprodutos.conexao.Conexao;
import java.awt.*;
import javax.swing.*;

/** Menu inicial: duas telas de gerenciamento independentes. */
public final class TelaPrincipal extends JFrame {
    private final JLabel labelConexao = new JLabel();

    public TelaPrincipal() {
        super("Laboratório de Software | Produtos e Categorias");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(670, 400);
        setMinimumSize(new Dimension(570, 340));
        setLocationRelativeTo(null);

        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(28, 38, 28, 38));
        GridBagConstraints gc = new GridBagConstraints();
        gc.gridx = 0; gc.fill = GridBagConstraints.HORIZONTAL; gc.weightx = 1;
        gc.insets = new Insets(5, 0, 8, 0);

        JLabel titulo = new JLabel("Gerenciamento de estoque");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD, 25f));
        gc.gridy=0;centro.add(titulo,gc);
        JLabel subtitulo = new JLabel("Projeto Java Swing + JDBC + MySQL | UFN");
        subtitulo.setForeground(new Color(95,105,115));
        gc.gridy=1;centro.add(subtitulo,gc);

        JButton produtos = new JButton("Produtos  |  Cadastrar, consultar, editar, excluir e buscar");
        produtos.setPreferredSize(new Dimension(500, 47));
        produtos.addActionListener(e -> new TelaProdutos().setVisible(true));
        gc.gridy=2;gc.insets=new Insets(23,0,8,0);centro.add(produtos,gc);

        JButton categorias = new JButton("Categorias  |  Cadastrar, listar, editar e excluir");
        categorias.setPreferredSize(new Dimension(500, 47));
        categorias.addActionListener(e -> new TelaCategorias(null).setVisible(true));
        gc.gridy=3;gc.insets=new Insets(5,0,8,0);centro.add(categorias,gc);

        JButton configurar = new JButton("Configurar ou testar a conexão MySQL");
        configurar.addActionListener(e -> {
            new TelaConfiguracao(this).setVisible(true);
            atualizarInfoConexao();
        });
        gc.gridy=4;gc.insets=new Insets(16,0,8,0);centro.add(configurar,gc);

        labelConexao.setForeground(new Color(90,100,110));
        gc.gridy=5;gc.insets=new Insets(5,0,0,0);centro.add(labelConexao,gc);
        add(centro,BorderLayout.CENTER);
        atualizarInfoConexao();
    }
    private void atualizarInfoConexao() {
        labelConexao.setText("Banco configurado: " + Conexao.getUsuario() + "@"
            + Conexao.getHost() + ":" + Conexao.getPorta() + "/" + Conexao.getBanco());
    }
}
