package cadastroprodutos;

import cadastroprodutos.telas.TelaPrincipal;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName()); }
        catch (Exception ignored) { /* Aparência padrão do Swing. */ }
        SwingUtilities.invokeLater(new Runnable() {
            @Override public void run() { new TelaPrincipal().setVisible(true); }
        });
    }
}
