import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Phase3GuiView extends JFrame implements Observer {

    private final Phase3Controller controller;

    // affichage
    private final JLabel lblTop = new JLabel("Phase 3 - MVC (GUI + CLI)");
    private final JTextArea area = new JTextArea(18, 70);

    // ===== SETUP panel =====
    private final JSpinner spPlayers = new JSpinner(new SpinnerNumberModel(2, 2, 6, 1));
    private final JButton btnPlayers = new JButton("Fixer nb joueurs");

    private final JTextField tfName = new JTextField(10);
    private final JComboBox<String> cbType = new JComboBox<>(new String[]{"HUMAIN", "ORDI"});
    private final JComboBox<String> cbStrat = new JComboBox<>(new String[]{"s1", "s2"});
    private final JButton btnAdd = new JButton("Ajouter joueur");

    private final JComboBox<String> cbVar = new JComboBox<>(new String[]{"normale", "joker", "couleur"});
    private final JButton btnVar = new JButton("Choisir variante");

    private final JButton btnStart = new JButton("START");

    // ===== GAME panel =====
    private final JButton btnNext = new JButton("Next");
    private final JButton btnOffer0 = new JButton("Offer: cacher 0");
    private final JButton btnOffer1 = new JButton("Offer: cacher 1");

    private final JComboBox<String> cbCibles = new JComboBox<>();
    private final JButton btnTakeV = new JButton("Prendre visible");
    private final JButton btnTakeC = new JButton("Prendre cachée");

    public Phase3GuiView(Phase3Controller controller) {
        super("Jest - Phase 3");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10,10));

        lblTop.setFont(lblTop.getFont().deriveFont(Font.BOLD, 16f));
        add(lblTop, BorderLayout.NORTH);

        area.setEditable(false);
        area.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(area), BorderLayout.CENTER);

        JPanel bottom = new JPanel(new GridLayout(2,1));

        // SETUP controls
        JPanel setup = new JPanel(new FlowLayout(FlowLayout.LEFT));
        setup.setBorder(BorderFactory.createTitledBorder("Configuration"));

        setup.add(new JLabel("Joueurs:"));
        setup.add(spPlayers);
        setup.add(btnPlayers);

        setup.add(new JLabel("Nom:"));
        setup.add(tfName);
        setup.add(cbType);
        setup.add(new JLabel("Strat:"));
        setup.add(cbStrat);
        setup.add(btnAdd);

        setup.add(new JLabel("Variante:"));
        setup.add(cbVar);
        setup.add(btnVar);

        setup.add(btnStart);

        // GAME controls
        JPanel game = new JPanel(new FlowLayout(FlowLayout.LEFT));
        game.setBorder(BorderFactory.createTitledBorder("Jeu"));

        game.add(btnNext);
        game.add(btnOffer0);
        game.add(btnOffer1);

        game.add(new JLabel("Cible:"));
        game.add(cbCibles);
        game.add(btnTakeV);
        game.add(btnTakeC);

        bottom.add(setup);
        bottom.add(game);

        add(bottom, BorderLayout.SOUTH);

        // listeners setup
        btnPlayers.addActionListener(e -> controller.setNbJoueurs((Integer) spPlayers.getValue()));

        btnAdd.addActionListener(e -> {
            String name = tfName.getText().trim();
            if (name.isEmpty()) return;

            String type = (String) cbType.getSelectedItem();
            if ("ORDI".equals(type)) {
                String s = (String) cbStrat.getSelectedItem();
                controller.addAI(name, "s2".equalsIgnoreCase(s) ? 2 : 1);
            } else {
                controller.addHuman(name);
            }
            tfName.setText("");
        });

        btnVar.addActionListener(e -> controller.setVariante((String) cbVar.getSelectedItem()));
        btnStart.addActionListener(e -> controller.startGame());

        // listeners game
        btnNext.addActionListener(e -> controller.next());
        btnOffer0.addActionListener(e -> controller.offerHiddenIndex(0));
        btnOffer1.addActionListener(e -> controller.offerHiddenIndex(1));

        btnTakeV.addActionListener(e -> {
            Object sel = cbCibles.getSelectedItem();
            if (sel != null) controller.takeOffer(sel.toString(), false);
        });
        btnTakeC.addActionListener(e -> {
            Object sel = cbCibles.getSelectedItem();
            if (sel != null) controller.takeOffer(sel.toString(), true);
        });

        pack();
        setLocationRelativeTo(null);

        // disable game buttons until needed
        setOfferEnabled(false);
        setTakeEnabled(false);
    }

    private void setOfferEnabled(boolean b) {
        btnOffer0.setEnabled(b);
        btnOffer1.setEnabled(b);
    }

    private void setTakeEnabled(boolean b) {
        cbCibles.setEnabled(b);
        btnTakeV.setEnabled(b);
        btnTakeC.setEnabled(b);
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Phase3Snapshot snap)) return;

        SwingUtilities.invokeLater(() -> {
            lblTop.setText("Phase=" + snap.phase + " | manche=" + snap.manche + " | joueur=" + (snap.joueurCourant == null ? "-" : snap.joueurCourant));
            area.setText(snap.toCliString());

            // refresh cibles
            cbCibles.removeAllItems();
            if (snap.ciblesDisponibles != null) {
                for (String c : snap.ciblesDisponibles) cbCibles.addItem(c);
            }

            boolean waitOffer = "WAIT_OFFER_HUMAN".equals(snap.phase) && snap.mainHumaine != null && !snap.mainHumaine.isEmpty();
            boolean waitTake  = "WAIT_TAKE_HUMAN".equals(snap.phase) && snap.ciblesDisponibles != null && !snap.ciblesDisponibles.isEmpty();

            setOfferEnabled(waitOffer);
            setTakeEnabled(waitTake);

            if (snap.partieFinie) {
                btnNext.setEnabled(false);
                setOfferEnabled(false);
                setTakeEnabled(false);
            }
        });
    }
}
