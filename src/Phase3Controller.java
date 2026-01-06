public class Phase3Controller {

    private final Phase3Model model;

    public Phase3Controller(Phase3Model model) {
        this.model = model;
    }

    // ===== CONFIG =====
    public void setNbJoueurs(int n) { model.setNbJoueurs(n); }

    public void addHuman(String nom) { model.addHuman(nom); }

    public void addAI(String nom, int strat) { model.addAI(nom, strat); }

    public void setVariante(String v) { model.setVariante(v); }

    public void startGame() { model.startGame(); }

    // ===== JEU =====
    public void next() { model.next(); }

    public void offerHiddenIndex(int idx) { model.humanCreateOffer(idx); }

    public void takeOffer(String cibleNom, boolean prendreCachee) { model.humanTakeOffer(cibleNom, prendreCachee); }
}
