package cadastroprodutos.telas;

import cadastroprodutos.conexao.Conexao;
import java.awt.*;
import java.sql.Connection;
import javax.swing.*;

/** Configura a conexão na sessão; nenhuma senha é persistida em arquivos. */
public final class TelaConfiguracao extends JDialog {
    private final JTextField txtHost = new JTextField(Conexao.getHost(),18);
    private final JTextField txtPorta = new JTextField(Conexao.getPorta(),18);
    private final JTextField txtBanco = new JTextField(Conexao.getBanco(),18);
    private final JTextField txtUsuario = new JTextField(Conexao.getUsuario(),18);
    private final JPasswordField txtSenha = new JPasswordField(18);
    private final JLabel status = new JLabel("Informe os dados do seu MySQL local.");

    public TelaConfiguracao(JFrame owner) {
        super(owner,"Configuração do MySQL",true);
        setSize(470,365);setLocationRelativeTo(owner);
        JPanel centro = new JPanel(new GridBagLayout());
        centro.setBorder(BorderFactory.createEmptyBorder(15,20,10,20));
        GridBagConstraints g = new GridBagConstraints();
        g.insets=new Insets(5,5,5,5);g.anchor=GridBagConstraints.WEST;
        JTextField[] campos={txtHost,txtPorta,txtBanco,txtUsuario,txtSenha};
        String[] nomes={"Host:","Porta:","Banco:","Usuário:","Senha:"};
        for(int i=0;i<campos.length;i++) {
            g.gridy=i;g.gridx=0;g.weightx=0;g.fill=GridBagConstraints.NONE;
            centro.add(new JLabel(nomes[i]),g);
            g.gridx=1;g.fill=GridBagConstraints.HORIZONTAL;g.weightx=1;
            centro.add(campos[i],g);
        }
        g.gridy=5;g.gridx=0;g.gridwidth=2;g.fill=GridBagConstraints.HORIZONTAL;
        status.setForeground(new Color(90,90,90));centro.add(status,g);

        JButton testar=new JButton("Salvar e testar conexão");
        testar.addActionListener(e -> testar());
        JButton fechar=new JButton("Fechar");fechar.addActionListener(e -> dispose());
        JPanel acoes=new JPanel(new FlowLayout(FlowLayout.RIGHT));
        acoes.add(fechar);acoes.add(testar);
        add(centro,BorderLayout.CENTER);add(acoes,BorderLayout.SOUTH);
    }
    private void testar() {
        char[] caracteres=txtSenha.getPassword();
        String senha=new String(caracteres);
        java.util.Arrays.fill(caracteres,'\0');
        try {
            Conexao.configurar(txtHost.getText().trim(),txtPorta.getText().trim(),
                txtBanco.getText().trim(),txtUsuario.getText().trim(),senha);
            try(Connection con=Conexao.abrir()) {
                status.setText("Conectado com sucesso!");
                Avisos.sucesso(this,"Conexão com MySQL estabelecida!");
                dispose();
            }
        } catch(Exception e) { Avisos.erro(this,e);status.setText("Conexão não validada."); }
    }
}
