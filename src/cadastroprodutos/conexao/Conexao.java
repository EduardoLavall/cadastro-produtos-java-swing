package cadastroprodutos.conexao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/** Configuração apenas em memória. A senha não é salva em arquivo nem em código. */
public final class Conexao {
    private static volatile String host = "localhost";
    private static volatile String porta = "3306";
    private static volatile String banco = "loja_swing";
    private static volatile String usuario = "root";
    private static volatile String senha = "";

    private Conexao() { }

    public static synchronized void configurar(String novoHost, String novaPorta,
                                               String novoBanco, String novoUsuario,
                                               String novaSenha) {
        if (novoHost == null || !novoHost.matches("[a-zA-Z0-9._-]+"))
            throw new IllegalArgumentException("Host inválido.");
        if (novaPorta == null || !novaPorta.matches("[0-9]{1,5}"))
            throw new IllegalArgumentException("Porta inválida.");
        int portaNumero = Integer.parseInt(novaPorta);
        if (portaNumero < 1 || portaNumero > 65535)
            throw new IllegalArgumentException("Porta fora do intervalo válido.");
        if (novoBanco == null || !novoBanco.matches("[a-zA-Z0-9_]+"))
            throw new IllegalArgumentException("Nome do banco inválido.");
        if (novoUsuario == null || novoUsuario.trim().isEmpty())
            throw new IllegalArgumentException("Usuário do banco é obrigatório.");
        host=novoHost; porta=novaPorta; banco=novoBanco;
        usuario=novoUsuario; senha=novaSenha == null ? "" : novaSenha;
    }

    public static String getHost() { return host; }
    public static String getPorta() { return porta; }
    public static String getBanco() { return banco; }
    public static String getUsuario() { return usuario; }

    public static Connection abrir() throws SQLException {
        String url = "jdbc:mysql://" + host + ":" + porta + "/" + banco
            + "?useUnicode=true&characterEncoding=utf8&connectionTimeZone=SERVER"
            + "&connectTimeout=3000&sslMode=DISABLED&allowPublicKeyRetrieval=true";
        return DriverManager.getConnection(url, usuario, senha);
    }
}
