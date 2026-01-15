package vue;

import controleur.Phase3Controller;
import modele.Phase3Snapshot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

public class Phase3GuiView extends JFrame implements Observer {

    private final Phase3Controller controller;

    private final JLabel lblTop = new JLabel("Phase 3 - MVC (GUI + CLI)");

    private final JTextArea areaLog = new JTextArea(10, 70);
    private final JPanel tablePanel = new JPanel();
    private final JPanel handPanel  = new JPanel();

    private final JSpinner spPlayers = new JSpinner(new SpinnerNumberModel(2, 2, 6, 1));
    private final JButton btnPlayers = new JButton("Fixer nb joueurs");
    private final JTextField tfName = new JTextField(10);
    private final JComboBox<String> cbType = new JComboBox<>(new String[]{"HUMAIN", "ORDI"});
    private final JComboBox<String> cbStrat = new JComboBox<>(new String[]{"s1", "s2"});
    private final JButton btnAdd = new JButton("Ajouter joueur");
    private final JComboBox<String> cbVar = new JComboBox<>(new String[]{"normale", "joker", "couleur"});
    private final JButton btnVar = new JButton("Choisir variante");
    private final JButton btnStart = new JButton("START");
    private final JButton btnSave = new JButton("Save");
    private final JButton btnLoad = new JButton("Load");


    private final JButton btnNext = new JButton("Next");

    private Phase3Snapshot lastSnap = null;

    public Phase3GuiView(Phase3Controller controller) {
        super("Jest - Phase 3 (Table cliquable)");
        this.controller = controller;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10));

        lblTop.setFont(lblTop.getFont().deriveFont(Font.BOLD, 16f));
        add(lblTop, BorderLayout.NORTH);

        JPanel center = new JPanel(new BorderLayout(10, 10));

        tablePanel.setBorder(BorderFactory.createTitledBorder("Table (offres) - Clique une carte pour prendre"));
        tablePanel.setLayout(new GridLayout(0, 2, 10, 10)); // 2 colonnes (adaptable)

        handPanel.setBorder(BorderFactory.createTitledBorder("Main (faire une offre) - Clique la carte à mettre CACHÉE"));
        handPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        center.add(tablePanel, BorderLayout.CENTER);
        center.add(handPanel, BorderLayout.SOUTH);


        add(center, BorderLayout.CENTER);

        areaLog.setEditable(false);
        areaLog.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
        add(new JScrollPane(areaLog), BorderLayout.EAST);

        JPanel bottom = new JPanel(new BorderLayout());

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

        JPanel actions = new JPanel(new FlowLayout(FlowLayout.LEFT));
        actions.setBorder(BorderFactory.createTitledBorder("Actions"));
        actions.add(btnNext);
        actions.add(btnSave);
        actions.add(btnLoad);


        bottom.add(setup, BorderLayout.CENTER);
        bottom.add(actions, BorderLayout.SOUTH);

        add(bottom, BorderLayout.SOUTH);

        btnPlayers.addActionListener(e -> controller.setNbJoueurs((Integer) spPlayers.getValue()));

        btnSave.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int result = fc.showSaveDialog(Phase3GuiView.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    String chemin = fc.getSelectedFile().getAbsolutePath();
                    controller.saveGame(chemin);
                }
            }
        });
        btnLoad.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int result = fc.showOpenDialog(Phase3GuiView.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    String chemin = fc.getSelectedFile().getAbsolutePath();
                    controller.loadGame(chemin);
                }
            }
        });


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

        // next
        btnNext.addActionListener(e -> controller.next());

        pack();
        setLocationRelativeTo(null);
    }


    private void rebuildTable(Phase3Snapshot snap) {
        tablePanel.removeAll();

        if (snap.offres == null) {
            tablePanel.revalidate();
            tablePanel.repaint();
            return;
        }


        for (Phase3Snapshot.OfferDTO o : snap.offres) {
            tablePanel.add(new OfferPanel(o, snap));
        }

        tablePanel.revalidate();
        tablePanel.repaint();
    }

    private void rebuildHand(Phase3Snapshot snap) {
        handPanel.removeAll();

        boolean waitOffer = "WAIT_OFFER_HUMAN".equals(snap.phase) && snap.mainHumaine != null && snap.mainHumaine.size() == 2;

        if (!waitOffer) {
            handPanel.add(new JLabel("—"));
            handPanel.revalidate();
            handPanel.repaint();
            return;
        }


        JButton c0 = new JButton("<html><b>modele.Carte 0</b><br>" + escapeHtml(snap.mainHumaine.get(0)) + "<br><i>(cliquer = CACHÉE)</i></html>");
        JButton c1 = new JButton("<html><b>modele.Carte 1</b><br>" + escapeHtml(snap.mainHumaine.get(1)) + "<br><i>(cliquer = CACHÉE)</i></html>");

        c0.addActionListener(e -> controller.offerHiddenIndex(0));
        c1.addActionListener(e -> controller.offerHiddenIndex(1));

        handPanel.add(c0);
        handPanel.add(c1);

        handPanel.revalidate();
        handPanel.repaint();
    }

    private class OfferPanel extends JPanel {
        OfferPanel(Phase3Snapshot.OfferDTO offer, Phase3Snapshot snap) {
            setLayout(new BorderLayout());
            setBorder(BorderFactory.createLineBorder(Color.GRAY));

            JLabel owner = new JLabel("modele.Offre de " + offer.owner);
            owner.setHorizontalAlignment(SwingConstants.CENTER);
            add(owner, BorderLayout.NORTH);

            JPanel cards = new JPanel(new GridLayout(1, 2, 8, 8));

            JButton btnVisible = new JButton("<html><b>Visible</b><br>" + escapeHtml(offer.visibleText) + "</html>");
            JButton btnHidden  = new JButton(offer.hasHiddenCard ? "<html><b>Cachée</b><br>???</html>" : "<html><b>Cachée</b><br>-</html>");

            boolean canTakePhase = "WAIT_TAKE_HUMAN".equals(snap.phase);
            boolean cibleOk = snap.ciblesDisponibles != null && snap.ciblesDisponibles.contains(offer.owner);
            boolean enabled = canTakePhase && cibleOk && offer.active;

            btnVisible.setEnabled(enabled);
            btnHidden.setEnabled(enabled && offer.hasHiddenCard);

            btnVisible.addActionListener(e -> controller.takeOffer(offer.owner, false));
            btnHidden.addActionListener(e -> controller.takeOffer(offer.owner, true));

            cards.add(btnVisible);
            cards.add(btnHidden);

            add(cards, BorderLayout.CENTER);


            JLabel footer = new JLabel(offer.active ? "ACTIVE" : "INACTIVE");
            footer.setHorizontalAlignment(SwingConstants.CENTER);
            add(footer, BorderLayout.SOUTH);
        }
    }

    private static String escapeHtml(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Phase3Snapshot snap)) return;
        lastSnap = snap;

        SwingUtilities.invokeLater(() -> {
            lblTop.setText("Phase=" + snap.phase + " | manche=" + snap.manche + " | joueur=" + (snap.joueurCourant == null ? "-" : snap.joueurCourant));
            areaLog.setText(snap.toCliString());

            rebuildTable(snap);
            rebuildHand(snap);

            if (snap.partieFinie) {
                btnNext.setEnabled(false);
                btnStart.setEnabled(false);
                btnAdd.setEnabled(false);
                btnPlayers.setEnabled(false);
                btnVar.setEnabled(false);
            }
        });
    }

}
