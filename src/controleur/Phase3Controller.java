package controleur;
import modele.Phase3Model;

public class Phase3Controller {
    /** Contrôleur MVC  */
    private final Phase3Model model;

    public Phase3Controller(Phase3Model model) {
        this.model = model;
    }

    public void setNbJoueurs(int n) { model.setNbJoueurs(n); }

    public void addHuman(String nom) { model.addHuman(nom); }

    public void addAI(String nom, int strat) { model.addAI(nom, strat); }

    public void setVariante(String v) { model.setVariante(v); }

    public void startGame() { model.startGame(); }

    public void next() { model.next(); }

    /** choisir la carte cachée pour créer l'offre */
    public void offerHiddenIndex(int idx) { model.humanCreateOffer(idx); }

    public void takeOffer(String cibleNom, boolean prendreCachee) { model.humanTakeOffer(cibleNom, prendreCachee); }

    public void saveGame(String file) { model.saveGame(file); }

    public void loadGame(String file) { model.loadGame(file); }

}

