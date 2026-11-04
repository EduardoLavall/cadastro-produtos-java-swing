package cadastroprodutos.telas;

    import java.awt.Component;
    import java.sql.SQLException;
    import javax.swing.JOptionPane;

    final class Avisos {
        private Avisos() { }

        static void erro(Component parent, Exception e) {
            String mensagem;
            if (e instanceof SQLException) {
                SQLException sql = (SQLException) e;
                String state = sql.getSQLState();
                if (sql.getMessage() != null && sql.getMessage().contains("No suitable driver")) {
                    mensagem = "Driver MySQL Connector/J não encontrado.\n"
                        + "Copie o arquivo .jar para lib/ e use a opção Executar do NetBeans/Ant.";
                } else if ("23000".equals(state) || (state != null && state.startsWith("23"))) {
                    mensagem = "Operação impedida: nome duplicado, categoria vinculada ou referência inválida.\n"
                        + "Verifique os dados e tente novamente.";
                } else if ("08001".equals(state) || (state != null && state.startsWith("08"))) {
                    mensagem = "Não foi possível conectar ao MySQL.\n"
                        + "Confira se ele está iniciado e use Configurar conexão na tela inicial.";
                } else {
                    mensagem = "Falha de banco de dados (SQLState " + state + ").\n"
                        + sql.getMessage();
                }
            } else mensagem = e.getMessage();
            JOptionPane.showMessageDialog(parent, mensagem, "Atenção", JOptionPane.ERROR_MESSAGE);
        }
        static void sucesso(Component parent, String texto) {
            JOptionPane.showMessageDialog(parent, texto, "Concluído", JOptionPane.INFORMATION_MESSAGE);
        }
        static boolean confirmar(Component parent, String texto) {
            return JOptionPane.showConfirmDialog(parent, texto, "Confirmar exclusão",
                JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE) == JOptionPane.YES_OPTION;
        }
    }
