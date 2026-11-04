package cadastroprodutos.telas;

import cadastroprodutos.dao.CategoriaDAO;
import cadastroprodutos.dao.ProdutoDAO;
import cadastroprodutos.beans.Categoria;
import cadastroprodutos.beans.Produto;
import cadastroprodutos.util.Validacao;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/** Tela produtos: CRUD completo, categoria alterável, listagem e busca por nome/categoria. */
public final class TelaProdutos extends JFrame {
    private final CategoriaDAO categoriaDAO=new CategoriaDAO();
    private final ProdutoDAO produtoDAO=new ProdutoDAO();
    private final JTextField txtNome=new JTextField(24);
    private final JTextField txtPreco=new JTextField("0,00",12);
    private final JTextField txtQuantidade=new JTextField("0",10);
    private final JComboBox<Categoria> cmbCategoria=new JComboBox<Categoria>();
    private final JTextField txtBusca=new JTextField(22);
    private final JComboBox<String> cmbCampo=new JComboBox<String>(new String[]{"Nome ou categoria","Nome","Categoria"});
    private final JLabel status=new JLabel("Dica: selecione um produto na tabela para editá-lo.");
    private final DefaultTableModel modelo=new DefaultTableModel(
        new String[]{"ID","Nome do produto","Preço (R$)","Estoque","Categoria"},0){
        @Override public boolean isCellEditable(int linha,int coluna){return false;}
    };
    private final JTable tabela=new JTable(modelo);
    private final NumberFormat moeda=NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
    private List<Produto> exibidos=new ArrayList<Produto>();
    private int idSelecionado=-1;

    public TelaProdutos(){
        super("Produtos | Java Swing + MySQL");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1050,760);setMinimumSize(new Dimension(800,600));
        setLocationByPlatform(true);

        JPanel superior=new JPanel(new BorderLayout(6,10));
        superior.setBorder(BorderFactory.createEmptyBorder(12,15,8,15));
        JLabel titulo=new JLabel("Cadastro de produtos");
        titulo.setFont(titulo.getFont().deriveFont(Font.BOLD,20f));
        superior.add(titulo,BorderLayout.NORTH);

        JPanel form=new JPanel(new GridBagLayout());
        form.setBorder(BorderFactory.createTitledBorder("Dados do produto"));
        GridBagConstraints g=new GridBagConstraints();
        g.insets=new Insets(5,7,5,7);g.anchor=GridBagConstraints.WEST;
        g.fill=GridBagConstraints.HORIZONTAL;
        addCampo(form,g,0,0,"Nome:",txtNome,3);
        addCampo(form,g,1,0,"Preço (R$):",txtPreco,1);
        addCampo(form,g,1,2,"Quantidade:",txtQuantidade,1);
        addCampo(form,g,2,0,"Categoria:",cmbCategoria,3);
        JButton gerenciar=new JButton("Gerenciar categorias...");
        gerenciar.addActionListener(e -> {
            new TelaCategorias(new Runnable(){
                @Override public void run(){recarregarCategorias();buscar();}
            }).setVisible(true);
        });
        g.gridy=2;g.gridx=4;g.gridwidth=1;g.weightx=0;
        form.add(gerenciar,g);

        JPanel acoesForm=new JPanel(new FlowLayout(FlowLayout.LEFT,10,6));
        JButton limpar=new JButton("Novo / Limpar");limpar.addActionListener(e -> limpar());
        JButton salvar=new JButton("Cadastrar produto");salvar.addActionListener(e -> cadastrar());
        JButton atualizar=new JButton("Atualizar produto");atualizar.addActionListener(e -> atualizar());
        JButton excluir=new JButton("Excluir produto");excluir.addActionListener(e -> excluir());
        acoesForm.add(limpar);acoesForm.add(salvar);acoesForm.add(atualizar);acoesForm.add(excluir);
        JPanel painelForm=new JPanel(new BorderLayout());
        painelForm.add(form,BorderLayout.CENTER);painelForm.add(acoesForm,BorderLayout.SOUTH);
        superior.add(painelForm,BorderLayout.CENTER);
        add(superior,BorderLayout.NORTH);

        JPanel painelTabela=new JPanel(new BorderLayout(5,8));
        painelTabela.setBorder(BorderFactory.createEmptyBorder(5,15,5,15));
        JPanel busca=new JPanel(new FlowLayout(FlowLayout.LEFT,8,4));
        busca.add(new JLabel("Buscar:"));busca.add(txtBusca);busca.add(cmbCampo);
        JButton buscar=new JButton("Pesquisar");buscar.addActionListener(e -> buscar());
        JButton mostrarTodos=new JButton("Mostrar todos");mostrarTodos.addActionListener(e -> {
            txtBusca.setText("");cmbCampo.setSelectedIndex(0);buscar();
        });
        busca.add(buscar);busca.add(mostrarTodos);
        txtBusca.addActionListener(e -> buscar());
        painelTabela.add(busca,BorderLayout.NORTH);
        tabela.setRowHeight(25);
        tabela.setAutoCreateRowSorter(true);
        tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        tabela.getSelectionModel().addListSelectionListener(e -> {
            if(e.getValueIsAdjusting())return;
            int linha=tabela.getSelectedRow();if(linha<0)return;
            int m=tabela.convertRowIndexToModel(linha);
            if(m<0 || m>=exibidos.size())return;
            preencherParaEdicao(exibidos.get(m));
        });
        JScrollPane scroll=new JScrollPane(tabela);
        scroll.setBorder(BorderFactory.createTitledBorder("Produtos cadastrados / resultado da pesquisa"));
        painelTabela.add(scroll,BorderLayout.CENTER);
        add(painelTabela,BorderLayout.CENTER);

        JPanel rodape=new JPanel(new BorderLayout());
        rodape.setBorder(BorderFactory.createEmptyBorder(7,16,12,16));
        rodape.add(status,BorderLayout.WEST);
        add(rodape,BorderLayout.SOUTH);

        recarregarCategorias();buscar();
        // Atualiza a lista de categorias após alternar de janela.
        addWindowFocusListener(new WindowAdapter(){
            @Override public void windowGainedFocus(WindowEvent e){
                // Evita consultar repetidamente em cada foco do mesmo componente.
                if(e.getOppositeWindow()!=null) recarregarCategorias();
            }
        });
    }

    private static void addCampo(JPanel painel,GridBagConstraints g,int linha,int coluna,
                                 String rotulo,JComponent componente,int largura){
        g.gridy=linha;g.gridx=coluna;g.gridwidth=1;g.weightx=0;
        painel.add(new JLabel(rotulo),g);
        g.gridx=coluna+1;g.gridwidth=largura;g.weightx=1;
        painel.add(componente,g);
    }
    private void recarregarCategorias(){
        int anterior=categoriaEscolhida();
        try {
            List<Categoria> categorias=categoriaDAO.listar();
            cmbCategoria.removeAllItems();
            for(Categoria c:categorias)cmbCategoria.addItem(c);
            selecionarCategoria(anterior);
            if(categorias.isEmpty())status.setText("Cadastre uma categoria antes de cadastrar produtos.");
        }catch(Exception e){Avisos.erro(this,e);}
    }
    private int categoriaEscolhida(){
        Categoria c=(Categoria)cmbCategoria.getSelectedItem();
        return c==null?-1:c.getId();
    }
    private void selecionarCategoria(int id){
        for(int i=0;i<cmbCategoria.getItemCount();i++){
            Categoria c=cmbCategoria.getItemAt(i);
            if(c.getId()==id){cmbCategoria.setSelectedIndex(i);return;}
        }
        if(cmbCategoria.getItemCount()>0)cmbCategoria.setSelectedIndex(0);
    }
    private void limpar(){
        idSelecionado=-1;
        txtNome.setText("");txtPreco.setText("0,00");txtQuantidade.setText("0");
        tabela.clearSelection();status.setText("Novo cadastro de produto.");
        txtNome.requestFocusInWindow();
    }
    private void preencherParaEdicao(Produto p){
        idSelecionado=p.getId();txtNome.setText(p.getNome());
        txtPreco.setText(p.getPreco().toPlainString().replace('.',','));
        txtQuantidade.setText(String.valueOf(p.getQuantidade()));
        selecionarCategoria(p.getCategoriaId());
        status.setText("Editando produto ID " + idSelecionado + ". Categoria pode ser alterada.");
    }
    private void buscar(){
        try {
            String campo=(String)cmbCampo.getSelectedItem();
            exibidos=produtoDAO.buscar(txtBusca.getText(),campo);
            modelo.setRowCount(0);
            for(Produto p:exibidos)modelo.addRow(new Object[]{
                p.getId(),p.getNome(),moeda.format(p.getPreco()),
                p.getQuantidade(),p.getCategoriaNome()});
            status.setText(exibidos.size()+" produto(s) encontrado(s).");
        }catch(Exception e){Avisos.erro(this,e);}
    }
    private String nome(){return Validacao.nomeProduto(txtNome.getText());}
    private BigDecimal preco(){return Validacao.preco(txtPreco.getText());}
    private int quantidade(){return Validacao.quantidade(txtQuantidade.getText());}
    private int categoria(){return Validacao.categoriaSelecionada(categoriaEscolhida());}
    private void cadastrar(){
        try {
            produtoDAO.inserir(nome(),preco(),quantidade(),categoria());
            buscar();limpar();Avisos.sucesso(this,"Produto cadastrado com sucesso!");
        }catch(Exception e){Avisos.erro(this,e);}
    }
    private void atualizar(){
        if(idSelecionado<0){Avisos.erro(this,new IllegalArgumentException("Selecione um produto na tabela."));return;}
        try {
            if(!produtoDAO.atualizar(idSelecionado,nome(),preco(),quantidade(),categoria()))
                throw new IllegalArgumentException("Produto não encontrado.");
            buscar();limpar();Avisos.sucesso(this,"Produto atualizado (inclusive sua categoria). ");
        }catch(Exception e){Avisos.erro(this,e);}
    }
    private void excluir(){
        if(idSelecionado<0){Avisos.erro(this,new IllegalArgumentException("Selecione um produto na tabela."));return;}
        if(!Avisos.confirmar(this,"Deseja realmente excluir o produto selecionado?"))return;
        try {
            if(!produtoDAO.excluir(idSelecionado))throw new IllegalArgumentException("Produto não encontrado.");
            buscar();limpar();Avisos.sucesso(this,"Produto excluído.");
        }catch(Exception e){Avisos.erro(this,e);}
    }
}
