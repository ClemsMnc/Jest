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


    @Override
    public void update(Observable o, Object arg) {
        if (arg instanceof Phase3Snapshot snap) {
            System.out.println("\n" + snap.toCliString());
        }
    }

}
