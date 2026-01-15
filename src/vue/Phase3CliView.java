package vue;

import controleur.Phase3Controller;
import modele.OfferDTO;
import modele.Phase3Snapshot;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Observable;
import java.util.Observer;

public class Phase3CliView implements Observer, Runnable {

    private final Phase3Controller controller;

    public Phase3CliView(Phase3Controller controller) {
        this.controller = controller;
        System.out.println("CLI active.");
        System.out.println("CONFIG: players <n> | human <Nom> | ai <Nom> s1|s2 | variant normale|joker|couleur | start");
        System.out.println("JEU   : next | offer 0|1 | take <Nom> visible|cachee");
    }

    @Override
    public void run() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            try {
                System.out.print("> ");
                String line = br.readLine();
                if (line == null) continue;
                line = line.trim();
                if (line.isEmpty()) continue;

                if (line.toLowerCase().startsWith("players")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 2) {
                        System.out.println("Usage: players <n>"); continue; }
                    controller.setNbJoueurs(Integer.parseInt(p[1]));
                    continue;
                }
                if (line.toLowerCase().startsWith("human")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 2) {
                        System.out.println("Usage: human <Nom>");
                        continue;
                    }
                    controller.addHuman(p[1]);
                    continue;
                }
                if (line.toLowerCase().startsWith("ai")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 3) { System.out.println("Usage: ai <Nom> s1|s2"); continue; }
                    int strat;
                    if (p[2].equalsIgnoreCase("s2")) {
                        strat = 2;
                    } else {
                        strat = 1;
                    }

                    controller.addAI(p[1], strat);
                    continue;
                }
                if (line.toLowerCase().startsWith("variant")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 2) {
                        System.out.println("Usage: variant normale|joker|couleur");
                        continue;
                    }
                    controller.setVariante(p[1]);
                    continue;
                }
                if (line.equalsIgnoreCase("start")) {
                    controller.startGame();
                    continue;
                }

                if (line.equalsIgnoreCase("next")) {
                    controller.next();
                    continue;
                }
                if (line.toLowerCase().startsWith("offer")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 2) {
                        System.out.println("Usage: offer 0|1");
                        continue;
                    }
                    controller.offerHiddenIndex(Integer.parseInt(p[1]));
                    continue;
                }
                if (line.toLowerCase().startsWith("take")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 3) {
                        System.out.println("Usage: take <Nom> visible|cachee");
                        continue;
                    }
                    boolean cachee = p[2].equalsIgnoreCase("cachee") || p[2].equalsIgnoreCase("c");
                    controller.takeOffer(p[1], cachee);
                    continue;
                }
                if (line.toLowerCase().startsWith("save")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 2) { System.out.println("Usage: save <fichier>"); continue; }
                    controller.saveGame(p[1]);
                    continue;
                }

                if (line.toLowerCase().startsWith("load")) {
                    String[] p = line.split("\\s+");
                    if (p.length < 2) { System.out.println("Usage: load <fichier>"); continue; }
                    controller.loadGame(p[1]);
                    continue;
                }
                System.out.println("Commande inconnue.");

            } catch (IOException e) {
                System.err.println("Erreur I/O: " + e.getMessage());
            } catch (Exception e) {
                System.err.println("Erreur: " + e.getMessage());
            }
        }
    }

    public String toCliString(Phase3Snapshot snap) {
        StringBuilder sb = new StringBuilder();
        sb.append("=== JEST | ").append(snap.getPhase()).append(" ===\n");
        if (snap.getMessage() != null && !snap.getMessage().isBlank()) sb.append(snap.getMessage()).append("\n");

        if (snap.getPhase().startsWith("SETUP")) {
            sb.append("\nCONFIG:\n");
            sb.append("- nb joueurs attendu: ").append(snap.getNbJoueursAttendu()).append("\n");
            sb.append("- nb joueurs actuel : ").append(snap.getNbJoueursActuel()).append("\n");
            sb.append("- variante          : ").append(snap.getVariante()).append("\n");
            sb.append("- joueurs:\n");
            for (String j : snap.getJoueursConfig()) sb.append("  * ").append(j).append("\n");

            sb.append("\nCommandes:\n");
            sb.append("players <n>\n");
            sb.append("human <Nom>\n");
            sb.append("ai <Nom> s1|s2\n");
            sb.append("variant normale|joker|couleur\n");
            sb.append("start\n");
            return sb.toString();
        }

        sb.append("\nMANCHE: ").append(snap.getManche()).append("\n");
        sb.append("modele.Joueur courant: ").append(snap.getJoueurCourant() == null ? "-" : snap.getJoueurCourant()).append("\n");

        sb.append("\nOFFRES:\n");
        for (OfferDTO o : snap.getOffres()) {
            sb.append("- ").append(o.owner)
                    .append(" | V=").append(o.visibleText)
                    .append(" | C=").append(o.hasHiddenCard ? "(cachée)" : "-")
                    .append(" | active=").append(o.active)
                    .append("\n");
        }

        sb.append("\nSCORES/JEST:\n");
        for (String s : snap.getScoresAffichage()) sb.append(s).append("\n");

        if (snap.getMainHumaine() != null && !snap.getMainHumaine().isEmpty()) {
            sb.append("\nMAIN (cliquer/cmd offer 0|1):\n");
            for (int i = 0; i < snap.getMainHumaine().size(); i++) sb.append(i).append(") ").append(snap.getMainHumaine().get(i)).append("\n");
        }

        if (snap.getCiblesDisponibles() != null && !snap.getCiblesDisponibles().isEmpty()) {
            sb.append("\nCIBLES DISPONIBLES:\n");
            for (String c : snap.getCiblesDisponibles()) sb.append("- ").append(c).append("\n");
            sb.append("Commande: take <Nom> visible|cachee\n");
        }

        sb.append("\nCommande générique: next\n");
        if (snap.isPartieFinie()) sb.append("\n*** FIN DE PARTIE ***\n");
        return sb.toString();
    }

    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Phase3Snapshot snap) {
            System.out.println("\n" + toCliString(snap));
        }
    }
}
