import controleur.Phase3Controller;
import modele.Phase3Model;
import vue.Phase3CliView;
import vue.Phase3GuiView;

public class Phase3Main {

    public static void main(String[] args) {

        // MODELE
        Phase3Model model = new Phase3Model();

        // CONTROLEUR
        Phase3Controller controller = new Phase3Controller(model);

        // VUE GUI
        Phase3GuiView gui = new Phase3GuiView(controller);
        model.addObserver(gui);

        // VUE CLI
        Phase3CliView cli = new Phase3CliView(controller);
        model.addObserver(cli);
        new Thread(cli, "CLI-Input").start();

        // Afficher GUI
        javax.swing.SwingUtilities.invokeLater(() -> gui.setVisible(true));

        // phase config
        model.publishState("Bienvenue le jeu JEST : configuration via GUI ou CLI.");
    }
}
