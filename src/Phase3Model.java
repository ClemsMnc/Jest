import java.util.*;
import java.util.stream.Collectors;

/**
 * MODELE MVC (Observable) :
 * - gère la config (sans Scanner)
 * - gère la partie (sans Scanner)
 * - notifie CLI + GUI avec un snapshot
 *
 * Conforme au TP : Observable / Observer + vue CLI en thread :contentReference[oaicite:1]{index=1}
 */
public class Phase3Model extends Observable {

    // ====== phases ======
    private enum Phase {
        SETUP,                 // configuration
        DEAL,                  // distribuer + créer les offres
        WAIT_OFFER_HUMAN,      // attendre offer 0/1
        TAKE,                  // prises (ordi auto)
        WAIT_TAKE_HUMAN,       // attendre take ...
        END
    }

    private Phase phase = Phase.SETUP;

    // ====== config ======
    private int nbJoueursAttendu = 0;
    private final ArrayList<Joueur> joueursConfig = new ArrayList<>();
    private VarianteVisitor variante = new VarianteNormale();

    // ====== partie ======
    private Partie partie = null;
    private int manche = 0;

    private final ArrayList<Humain> humansWaitingOffer = new ArrayList<>();
    private Humain currentHumanOffer = null;

    private ArrayList<Joueur> joueursAyantPasJoue = new ArrayList<>();
    private Joueur joueurCourant = null;

    private String lastMessage = "";

    // =======================
    //   PUBLIC API (Controller)
    // =======================

    public synchronized void publishState(String msg) {
        lastMessage = msg == null ? "" : msg;
        publish();
    }

    public synchronized void setNbJoueurs(int n) {
        if (phase != Phase.SETUP) { publishState("Impossible: la partie a déjà démarré."); return; }
        if (n < 2 || n > 6) { publishState("nb joueurs invalide (2..6)."); return; }
        nbJoueursAttendu = n;
        publishState("nb joueurs attendu = " + n);
    }

    public synchronized void addHuman(String nom) {
        if (phase != Phase.SETUP) { publishState("Impossible: la partie a déjà démarré."); return; }
        if (nbJoueursAttendu == 0) { publishState("D'abord: players <n>."); return; }
        if (joueursConfig.size() >= nbJoueursAttendu) { publishState("Déjà assez de joueurs."); return; }
        if (nom == null || nom.isBlank()) { publishState("Nom invalide."); return; }
        if (nameExists(nom)) { publishState("Nom déjà utilisé."); return; }

        joueursConfig.add(new Humain(nom.trim()));
        publishState("Ajout humain: " + nom);
    }

    public synchronized void addAI(String nom, int strat) {
        if (phase != Phase.SETUP) { publishState("Impossible: la partie a déjà démarré."); return; }
        if (nbJoueursAttendu == 0) { publishState("D'abord: players <n>."); return; }
        if (joueursConfig.size() >= nbJoueursAttendu) { publishState("Déjà assez de joueurs."); return; }
        if (nom == null || nom.isBlank()) { publishState("Nom invalide."); return; }
        if (nameExists(nom)) { publishState("Nom déjà utilisé."); return; }

        Ordinateur o = new Ordinateur(nom.trim());
        if (strat == 2) o.setStrategie(new Strategie2());
        else o.setStrategie(new Strategie1());
        joueursConfig.add(o);

        publishState("Ajout ordi: " + nom + " (s" + (strat == 2 ? "2" : "1") + ")");
    }

    public synchronized void setVariante(String v) {
        if (phase != Phase.SETUP) { publishState("Impossible: la partie a déjà démarré."); return; }
        if (v == null) { publishState("Variante invalide."); return; }

        String s = v.trim().toLowerCase();
        switch (s) {
            case "normale" -> variante = new VarianteNormale();
            case "joker" -> variante = new VarianteJoker();
            case "couleur" -> variante = new VarianteCouleur();
            default -> { publishState("Variante inconnue (normale|joker|couleur)."); return; }
        }
        publishState("Variante = " + s);
    }

    public synchronized void startGame() {
        if (phase != Phase.SETUP) { publishState("Déjà démarré."); return; }
        if (nbJoueursAttendu == 0) { publishState("D'abord: players <n>."); return; }
        if (joueursConfig.size() != nbJoueursAttendu) {
            publishState("Il manque des joueurs (" + joueursConfig.size() + "/" + nbJoueursAttendu + ").");
            return;
        }

        // construire Partie avec ta logique métier, sans Scanner
        partie = new Partie();
        partie.setCartes(partie.creerPaquetBase());
        partie.setJoueurs(new ArrayList<>(joueursConfig));
        partie.configurerTrophees(); // pas de Scanner
        manche = 0;

        phase = Phase.DEAL;
        publishState("Partie démarrée. (next pour avancer si besoin)");
        autoAdvanceUntilHumanNeeded();
        publish();
    }

    public synchronized void next() {
        if (phase == Phase.END) return;
        if (phase == Phase.SETUP) { publishState("En config. Utilise start."); return; }
        autoAdvanceUntilHumanNeeded();
        publish();
    }

    // HUMAIN : choix carte cachée
    public synchronized void humanCreateOffer(int idx) {
        if (phase != Phase.WAIT_OFFER_HUMAN || currentHumanOffer == null) {
            publishState("Pas en attente de création d'offre.");
            return;
        }
        if (idx != 0 && idx != 1) { publishState("Index invalide (0 ou 1)."); return; }

        Carte c0 = currentHumanOffer.getMain().getCartes().get(0);
        Carte c1 = currentHumanOffer.getMain().getCartes().get(1);

        Carte cachee = (idx == 0) ? c0 : c1;
        Carte visible = (idx == 0) ? c1 : c0;

        currentHumanOffer.setOffre(new Offre(visible, cachee));
        currentHumanOffer.setMain(new Paquet()); // comme Humain.faireUneOffre()

        humansWaitingOffer.remove(currentHumanOffer);
        String done = currentHumanOffer.getNom();
        currentHumanOffer = null;

        if (!humansWaitingOffer.isEmpty()) {
            currentHumanOffer = humansWaitingOffer.getFirst();
            phase = Phase.WAIT_OFFER_HUMAN;
            publishState("Offre OK pour " + done + ". Au tour de " + currentHumanOffer.getNom() + " (offer 0/1).");
            return;
        }

        // toutes les offres prêtes -> prises
        initTakePhase();
        phase = Phase.TAKE;
        autoAdvanceUntilHumanNeeded();
        publish();
    }

    // HUMAIN : prise
    public synchronized void humanTakeOffer(String cibleNom, boolean prendreCachee) {
        if (phase != Phase.WAIT_TAKE_HUMAN || joueurCourant == null) {
            publishState("Pas en attente d'une prise humaine.");
            return;
        }

        Joueur cible = findPlayerByName(cibleNom);
        if (cible == null) { publishState("Cible introuvable: " + cibleNom); return; }

        ArrayList<Joueur> joueursDispo = computeJoueursDispo(joueurCourant);
        if (!joueursDispo.contains(cible)) { publishState("Offre non prenable maintenant."); return; }

        // même logique que Partie.selectionnerOffres()
        joueurCourant.prendreUneOffre(cible, prendreCachee, partie.getJoueurs());
        joueursAyantPasJoue.remove(joueurCourant);
        joueurCourant = cible;

        publishState("Prise OK. Prochain joueur: " + joueurCourant.getNom());
        if (!partie.encoreDesOffresDispo()) {
            endRoundOrGame();
            publish();
            return;
        }

        phase = Phase.TAKE;
        autoAdvanceUntilHumanNeeded();
        publish();
    }

    // =======================
    //   LOGIQUE INTERNE (jeu)
    // =======================

    private void autoAdvanceUntilHumanNeeded() {
        while (true) {
            if (phase == Phase.END) return;

            if (phase == Phase.DEAL) {
                if (partie.estTerminee()) {
                    endGame();
                    return;
                }
                startRound();
                continue;
            }

            if (phase == Phase.WAIT_OFFER_HUMAN) return;

            if (phase == Phase.TAKE) {

                if (!partie.encoreDesOffresDispo()) {
                    endRoundOrGame();
                    continue;
                }

                if (!joueursAyantPasJoue.contains(joueurCourant)) {
                    joueurCourant = joueursAyantPasJoue.getFirst();
                }

                ArrayList<Joueur> joueursDispo = computeJoueursDispo(joueurCourant);
                if (joueursDispo.isEmpty()) {
                    endRoundOrGame();
                    continue;
                }

                if (joueurCourant instanceof Ordinateur) {
                    // comme ta CLI : ordi joue, puis next = celui dont l’offre est devenue inactive
                    joueurCourant.prendreUneOffre(null, false, joueursDispo);
                    joueursAyantPasJoue.remove(joueurCourant);

                    for (Joueur j : joueursDispo) {
                        Offre o = j.getOffre();
                        if (o != null && !o.getStatutOffre()) {
                            joueurCourant = j;
                            break;
                        }
                    }

                    lastMessage = "Ordinateur a joué. Prochain: " + (joueurCourant == null ? "-" : joueurCourant.getNom());
                    continue;
                }

                phase = Phase.WAIT_TAKE_HUMAN;
                lastMessage = "Tour de " + joueurCourant.getNom() + " : take <Nom> visible|cachee";
                return;
            }

            if (phase == Phase.WAIT_TAKE_HUMAN) return;
        }
    }

    private void startRound() {
        manche++;

        // même logique que ta jouerPartie() mais sans Scanner :
        partie.recupererCartesDesOffres();
        partie.constituerPaquetManche();
        partie.distribuerCartes();

        // offres : ordi auto, humains en attente
        humansWaitingOffer.clear();
        for (Joueur j : partie.getJoueurs()) {
            if (j instanceof Ordinateur) {
                j.setOffre(j.faireUneOffre());
            } else if (j instanceof Humain h) {
                humansWaitingOffer.add(h);
            }
        }

        if (!humansWaitingOffer.isEmpty()) {
            currentHumanOffer = humansWaitingOffer.getFirst();
            phase = Phase.WAIT_OFFER_HUMAN;
            lastMessage = "Manche " + manche + " : " + currentHumanOffer.getNom() + " doit choisir (offer 0/1).";
        } else {
            initTakePhase();
            phase = Phase.TAKE;
            lastMessage = "Manche " + manche + " : début des prises.";
        }
    }

    private void initTakePhase() {
        joueursAyantPasJoue = new ArrayList<>(partie.getJoueurs());
        joueurCourant = partie.determinerPremierJoueur();
        if (joueurCourant == null) throw new IllegalStateException("Impossible de déterminer le premier joueur");
    }

    private void endRoundOrGame() {
        if (partie.estTerminee()) {
            // fin comme ta CLI : chacun prend la dernière carte restante
            for (Joueur j : partie.getJoueurs()) {
                Offre o = j.getOffre();
                if (o == null) continue;

                Carte r = (o.getCarteFaceAvant() != null) ? o.getCarteFaceAvant() : o.getCarteFaceCachee();
                if (r != null) j.getJest().ajouterCarte(r);

                o.setStatutOffre(false);
                o.setCarteFaceAvant(null);
                o.setCarteFaceCachee(null);
            }
            endGame();
        } else {
            phase = Phase.DEAL;
            lastMessage = "Fin manche " + manche + ". (next pour continuer)";
        }
    }

    private void endGame() {
        partie.distribuerTrophees();
        partie.accept(variante); // variante choisie en config
        phase = Phase.END;
        lastMessage = "FIN DE PARTIE.";
    }

    // =======================
    //   HELPERS + SNAPSHOT
    // =======================

    private boolean nameExists(String nom) {
        for (Joueur j : joueursConfig) if (j.getNom().equalsIgnoreCase(nom)) return true;
        return false;
    }

    private Joueur findPlayerByName(String name) {
        for (Joueur j : partie.getJoueurs()) {
            if (j.getNom().equalsIgnoreCase(name)) return j;
        }
        return null;
    }

    // identique à ton selectionnerOffres() : cibles prenable (statutOffre true), sauf soi (sauf dernier)
    private ArrayList<Joueur> computeJoueursDispo(Joueur joueurSuivant) {
        ArrayList<Joueur> joueursDispo = new ArrayList<>();
        for (Joueur j : partie.getJoueurs()) {
            boolean ok = (j != joueurSuivant && j.getOffre() != null && j.getOffre().getStatutOffre())
                    || (j == joueurSuivant && joueursAyantPasJoue.size() == 1);
            if (ok) joueursDispo.add(j);
        }
        return joueursDispo;
    }

    private void publish() {
        setChanged();
        notifyObservers(buildSnapshot());
    }

    private Phase3Snapshot buildSnapshot() {

        // --- setup info ---
        ArrayList<String> cfg = new ArrayList<>();
        for (Joueur j : joueursConfig) {
            if (j instanceof Ordinateur o) {
                String st = (o.getStrategie() instanceof Strategie2) ? "s2" : "s1";
                cfg.add(j.getNom() + " [AI " + st + "]");
            } else {
                cfg.add(j.getNom() + " [HUMAN]");
            }
        }
        String varName = (variante instanceof VarianteJoker) ? "joker" : (variante instanceof VarianteCouleur) ? "couleur" : "normale";

        // --- game info ---
        int m = manche;
        String jc = null;
        ArrayList<String> main = new ArrayList<>();
        ArrayList<String> cibles = new ArrayList<>();
        ArrayList<String> offres = new ArrayList<>();
        ArrayList<String> scores = new ArrayList<>();
        boolean fin = (phase == Phase.END);

        if (phase == Phase.WAIT_OFFER_HUMAN && currentHumanOffer != null) {
            jc = currentHumanOffer.getNom();
            for (Carte c : currentHumanOffer.getMain().getCartes()) main.add(String.valueOf(c));
        } else if (joueurCourant != null) {
            jc = joueurCourant.getNom();
        }

        if (partie != null) {
            // offres affichage
            for (Joueur j : partie.getJoueurs()) {
                Offre o = j.getOffre();
                if (o == null) {
                    offres.add("- " + j.getNom() + " | (pas d'offre)");
                } else {
                    String vis = String.valueOf(o.getCarteFaceAvant());
                    String cach = (o.getCarteFaceCachee() == null) ? "-" : "(cachée)";
                    offres.add("- " + j.getNom() + " | V=" + vis + " | C=" + cach + " | active=" + o.getStatutOffre());
                }
            }
            // scores/jest
            for (Joueur j : partie.getJoueurs()) {
                scores.add("- " + j.getNom() + " | score=" + j.getScore() + " | jest=" + j.getJest().getCartes());
            }

            if (phase == Phase.WAIT_TAKE_HUMAN && joueurCourant != null) {
                for (Joueur j : computeJoueursDispo(joueurCourant)) cibles.add(j.getNom());
            }
        }

        return new Phase3Snapshot(
                phase == Phase.SETUP ? "SETUP" : phase.name(),
                lastMessage,
                nbJoueursAttendu,
                joueursConfig.size(),
                varName,
                cfg,
                m,
                jc,
                main,
                cibles,
                offres,
                scores,
                fin
        );
    }
}
