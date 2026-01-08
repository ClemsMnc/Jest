import java.util.*;

public class Phase3Model extends Observable {

    private enum Phase { SETUP, DEAL, WAIT_OFFER_HUMAN, TAKE, WAIT_TAKE_HUMAN, END }
    private Phase phase = Phase.SETUP;

    // SETUP
    private int nbJoueursAttendu = 0;
    private final ArrayList<Joueur> joueursConfig = new ArrayList<>();
    private VarianteVisitor variante = new VarianteNormale();

    // JEU
    private Partie partie = null;
    private int manche = 0;

    private final ArrayList<Humain> humansWaitingOffer = new ArrayList<>();
    private Humain currentHumanOffer = null;

    private ArrayList<Joueur> joueursAyantPasJoue = new ArrayList<>();
    private Joueur joueurCourant = null;

    private String lastMessage = "";

    public synchronized void publishState(String msg) {
        lastMessage = msg == null ? "" : msg;
        publish();
    }

    public synchronized void setNbJoueurs(int n) {
        if (phase != Phase.SETUP) {
            publishState("Impossible: déjà démarré.");
            return;
        }
        if (n < 2 || n > 6) {
            publishState("nb joueurs invalide (2..6).");
            return;
        }
        nbJoueursAttendu = n;
        publishState("nb joueurs attendu = " + n);
    }

    public synchronized void addHuman(String nom) {
        if (phase != Phase.SETUP) {
            publishState("Impossible: déjà démarré."); return;
        }
        if (nbJoueursAttendu == 0) {
            publishState("D'abord: players <n>."); return;
        }
        if (joueursConfig.size() >= nbJoueursAttendu) {
            publishState("Déjà assez de joueurs.");
            return;
        }
        if (nom == null || nom.isBlank()) { publishState("Nom invalide.");
            return;
        }
        if (nameExists(nom)) { publishState("Nom déjà utilisé.");
            return;
        }
        joueursConfig.add(new Humain(nom.trim()));
        publishState("Ajout humain: " + nom);
    }

    public synchronized void addAI(String nom, int strat) {
        if (phase != Phase.SETUP) { publishState("Impossible: déjà démarré.");
            return;
        }
        if (nbJoueursAttendu == 0) { publishState("D'abord: players <n>.");
            return;
        }
        if (joueursConfig.size() >= nbJoueursAttendu) {
            publishState("Déjà assez de joueurs.");
            return;
        }
        if (nom == null || nom.isBlank()) {
            publishState("Nom invalide.");
            return;
        }
        if (nameExists(nom)) { publishState("Nom déjà utilisé.");
            return;
        }

        Ordinateur o = new Ordinateur(nom.trim());
        o.setStrategie(strat == 2 ? new Strategie2() : new Strategie1());
        joueursConfig.add(o);

        publishState("Ajout ordi: " + nom + " (s" + (strat == 2 ? "2" : "1") + ")");
    }

    public synchronized void setVariante(String v) {
        if (phase != Phase.SETUP) { publishState("Impossible: déjà démarré.");
            return;
        }
        if (v == null) {
            publishState("Variante invalide.");
            return;
        }
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
        if (phase != Phase.SETUP) {
            publishState("Déjà démarré.");
            return;
        }
        if (nbJoueursAttendu == 0) {
            publishState("D'abord: players <n>.");
            return;
        }
        if (joueursConfig.size() != nbJoueursAttendu) {
            publishState("Il manque des joueurs (" + joueursConfig.size() + "/" + nbJoueursAttendu + ").");
            return;
        }

        partie = new Partie();
        partie.setCartes(partie.creerPaquetBase());
        partie.setJoueurs(new ArrayList<>(joueursConfig));
        partie.configurerTrophees();

        manche = 0;
        phase = Phase.DEAL;

        publishState("Partie démarrée.");
        autoAdvanceUntilHumanNeeded();
        publish();
    }

    public synchronized void next() {
        if (phase == Phase.END) return;
        if (phase == Phase.SETUP) {
            publishState("En config. Utilise start.");
            return;
        }
        autoAdvanceUntilHumanNeeded();
        publish();
    }

    public synchronized void humanCreateOffer(int idx) {
        if (phase != Phase.WAIT_OFFER_HUMAN || currentHumanOffer == null) {
            publishState("Pas en attente d'offre.");
            return;
        }
        if (idx != 0 && idx != 1) { publishState("Index invalide (0|1).");
            return;
        }

        Carte c0 = currentHumanOffer.getMain().getCartes().get(0);
        Carte c1 = currentHumanOffer.getMain().getCartes().get(1);

        Carte cachee = (idx == 0) ? c0 : c1;
        Carte visible = (idx == 0) ? c1 : c0;

        currentHumanOffer.setOffre(new Offre(visible, cachee));
        currentHumanOffer.setMain(new Paquet());

        humansWaitingOffer.remove(currentHumanOffer);
        String done = currentHumanOffer.getNom();
        currentHumanOffer = null;

        if (!humansWaitingOffer.isEmpty()) {
            currentHumanOffer = humansWaitingOffer.getFirst();
            phase = Phase.WAIT_OFFER_HUMAN;
            publishState("Offre OK pour " + done + ". Au tour de " + currentHumanOffer.getNom());
            return;
        }

        initTakePhase();
        phase = Phase.TAKE;
        autoAdvanceUntilHumanNeeded();
        publish();
    }

    public synchronized void humanTakeOffer(String cibleNom, boolean prendreCachee) {
        if (phase != Phase.WAIT_TAKE_HUMAN || joueurCourant == null) {
            publishState("Pas en attente de prise.");
            return;
        }

        Joueur cible = findPlayerByName(cibleNom);
        if (cible == null) { publishState("Cible introuvable: " + cibleNom);
            return;
        }

        ArrayList<Joueur> dispo = computeJoueursDispo(joueurCourant);
        if (!dispo.contains(cible)) { publishState("Offre non prenable maintenant.");
            return; }

        joueurCourant.prendreUneOffre(cible, prendreCachee, partie.getJoueurs());
        joueursAyantPasJoue.remove(joueurCourant);
        joueurCourant = cible;

        publishState("Prise OK. Prochain: " + joueurCourant.getNom());

        if (!partie.encoreDesOffresDispo()) {
            endRoundOrGame();
            publish();
            return;
        }

        phase = Phase.TAKE;
        autoAdvanceUntilHumanNeeded();
        publish();
    }

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

                ArrayList<Joueur> dispo = computeJoueursDispo(joueurCourant);
                if (dispo.isEmpty()) {
                    endRoundOrGame();
                    continue;
                }

                if (joueurCourant instanceof Ordinateur) {
                    joueurCourant.prendreUneOffre(null, false, dispo);
                    joueursAyantPasJoue.remove(joueurCourant);

                    // prochain = celui dont l'offre vient d'être fermée (comme ta CLI)
                    for (Joueur j : dispo) {
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
                lastMessage = "Tour de " + joueurCourant.getNom() + " : clique sur une carte d’une offre.";
                return;
            }

            if (phase == Phase.WAIT_TAKE_HUMAN) return;
        }
    }

    private void startRound() {
        manche++;

        partie.recupererCartesDesOffres();
        partie.constituerPaquetManche();
        partie.distribuerCartes();

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
            lastMessage = "Manche " + manche + " : " + currentHumanOffer.getNom() + " choisit la carte cachée (clic).";
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
        partie.accept(variante);
        phase = Phase.END;
        lastMessage = "FIN DE PARTIE.";
    }

    // ==== helpers ====

    private boolean nameExists(String nom) {
        for (Joueur j : joueursConfig) if (j.getNom().equalsIgnoreCase(nom)) return true;
        return false;
    }

    private Joueur findPlayerByName(String name) {
        for (Joueur j : partie.getJoueurs()) if (j.getNom().equalsIgnoreCase(name)) return j;
        return null;
    }

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

        int m = manche;
        String jc = null;

        ArrayList<String> main = new ArrayList<>();
        ArrayList<String> cibles = new ArrayList<>();
        ArrayList<Phase3Snapshot.OfferDTO> offres = new ArrayList<>();
        ArrayList<String> scores = new ArrayList<>();

        boolean fin = (phase == Phase.END);

        if (phase == Phase.WAIT_OFFER_HUMAN && currentHumanOffer != null) {
            jc = currentHumanOffer.getNom();
            for (Carte c : currentHumanOffer.getMain().getCartes()) main.add(String.valueOf(c));
        } else if (joueurCourant != null) {
            jc = joueurCourant.getNom();
        }

        if (partie != null) {
            for (Joueur j : partie.getJoueurs()) {
                Offre o = j.getOffre();
                if (o == null) {
                    offres.add(new Phase3Snapshot.OfferDTO(j.getNom(), "(pas d'offre)", false, false));
                } else {
                    String visible = String.valueOf(o.getCarteFaceAvant());
                    boolean hasHidden = (o.getCarteFaceCachee() != null);
                    offres.add(new Phase3Snapshot.OfferDTO(j.getNom(), visible, hasHidden, o.getStatutOffre()));
                }
            }

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
