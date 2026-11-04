package cadastroprodutos.telas;

    import cadastroprodutos.dao.CategoriaDAO;
    import cadastroprodutos.beans.Categoria;
    import cadastroprodutos.util.Validacao;
    import java.awt.*;
    import java.awt.event.WindowAdapter;
    import java.awt.event.WindowEvent;
    import java.util.List;
    import javax.swing.*;
    import javax.swing.event.ListSelectionEvent;
    import javax.swing.table.DefaultTableModel;

    /** Tela separada: cadastro, edição, listagem e exclusão de categorias. */
    public final class TelaCategorias extends JFrame {
        private final CategoriaDAO dao = new CategoriaDAO();
        private final JTextField txtNome=new JTextField(25);
        private final JLabel status=new JLabel("Selecione uma categoria para editar ou excluir.");
        private final DefaultTableModel modelo=new DefaultTableModel(new String[]{"ID","Nome"},0) {
            @Override public boolean isCellEditable(int l,int c){return false;}
        };
        private final JTable tabela=new JTable(modelo);
        private int idSelecionado=-1;

        public TelaCategorias(Runnable aoFechar) {
            super("Categorias | Java Swing");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            setSize(760,550);setMinimumSize(new Dimension(640,460));
            setLocationByPlatform(true);
            JPanel topo=new JPanel(new BorderLayout(8,8));
            topo.setBorder(BorderFactory.createEmptyBorder(15,16,10,16));
            JLabel titulo=new JLabel("Gerenciamento de categorias");
            titulo.setFont(titulo.getFont().deriveFont(Font.BOLD,20f));
            topo.add(titulo,BorderLayout.NORTH);
            JPanel form=new JPanel(new BorderLayout(8,0));
            form.add(new JLabel("Nome da categoria:"),BorderLayout.WEST);
            form.add(txtNome,BorderLayout.CENTER);
            topo.add(form,BorderLayout.CENTER);
            add(topo,BorderLayout.NORTH);

            tabela.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
            tabela.setRowHeight(25);
            tabela.getSelectionModel().addListSelectionListener((ListSelectionEvent e) -> {
                if (e.getValueIsAdjusting()) return;
                int linha=tabela.getSelectedRow();
                if(linha<0)return;
                int indice=tabela.convertRowIndexToModel(linha);
                idSelecionado=(Integer)modelo.getValueAt(indice,0);
                txtNome.setText(String.valueOf(modelo.getValueAt(indice,1)));
                status.setText("Editando categoria ID: " + idSelecionado);
            });
            JScrollPane scroll=new JScrollPane(tabela);
            scroll.setBorder(BorderFactory.createTitledBorder("Categorias cadastradas"));
            add(scroll,BorderLayout.CENTER);

            JPanel baixo=new JPanel(new BorderLayout());
            baixo.setBorder(BorderFactory.createEmptyBorder(9,12,12,12));
            JPanel botoes=new JPanel(new FlowLayout(FlowLayout.LEFT));
            JButton novo=new JButton("Novo / Limpar");novo.addActionListener(e -> limpar());
            JButton cadastrar=new JButton("Cadastrar");cadastrar.addActionListener(e -> cadastrar());
            JButton atualizar=new JButton("Atualizar");atualizar.addActionListener(e -> atualizar());
            JButton excluir=new JButton("Excluir");excluir.addActionListener(e -> excluir());
            JButton listar=new JButton("Recarregar lista");listar.addActionListener(e -> listar());
            botoes.add(novo);botoes.add(cadastrar);botoes.add(atualizar);
            botoes.add(excluir);botoes.add(listar);
            baixo.add(botoes,BorderLayout.NORTH);baixo.add(status,BorderLayout.SOUTH);
            add(baixo,BorderLayout.SOUTH);
            if(aoFechar!=null) addWindowListener(new WindowAdapter(){
                @Override public void windowClosed(WindowEvent e){aoFechar.run();}
            });
            listar();
        }
        private void limpar() {
            idSelecionado=-1;txtNome.setText("");tabela.clearSelection();
            status.setText("Novo cadastro de categoria.");txtNome.requestFocusInWindow();
        }
        private void listar() {
            idSelecionado=-1;
            tabela.clearSelection();
            try {
                List<Categoria> todas=dao.listar();
                modelo.setRowCount(0);
                for(Categoria c:todas)modelo.addRow(new Object[]{c.getId(),c.getNome()});
                status.setText(todas.size()+" categoria(s) cadastrada(s).");
            } catch(Exception e){Avisos.erro(this,e);}
        }
        private void cadastrar(){
            try {
                String nome=Validacao.nomeCategoria(txtNome.getText());
                dao.inserir(nome);listar();limpar();Avisos.sucesso(this,"Categoria cadastrada.");
            }catch(Exception e){Avisos.erro(this,e);}
        }
        private void atualizar(){
            if(idSelecionado<0){Avisos.erro(this,new IllegalArgumentException("Selecione uma categoria na tabela."));return;}
            try {
                String nome=Validacao.nomeCategoria(txtNome.getText());
                if(!dao.atualizar(idSelecionado,nome))throw new IllegalArgumentException("Categoria não encontrada.");
                listar();limpar();Avisos.sucesso(this,"Categoria atualizada.");
            }catch(Exception e){Avisos.erro(this,e);}
        }
        private void excluir(){
            if(idSelecionado<0){Avisos.erro(this,new IllegalArgumentException("Selecione uma categoria na tabela."));return;}
            if(!Avisos.confirmar(this,"Excluir a categoria selecionada?\n"
                + "Se houver produtos vinculados, a exclusão será impedida."))return;
            try {
                if(!dao.excluir(idSelecionado))throw new IllegalArgumentException("Categoria não encontrada.");
                listar();limpar();Avisos.sucesso(this,"Categoria excluída.");
            }catch(Exception e){Avisos.erro(this,e);}
        }
    }
