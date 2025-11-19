import java.util.ArrayList;

public class Paquet {
    ArrayList<Carte> cartes;

    public Paquet() {
        this.cartes = new ArrayList<Carte>();
    }

    public Paquet(ArrayList<Carte> cartes) {
        this.cartes = cartes;
    }

    public void melanger() {
        java.util.Collections.shuffle(this.cartes);
    }

    public void ajouterCarte(Carte carte) {
        this.cartes.add(carte);
    }

    public void retirerCarte(Carte carte) {
        this.cartes.remove(carte);
    }

    public Carte getCarteDessus() {
        return this.cartes.removeLast();
    }

    public void distribuer(Joueur joueur, int nbCartes){
        for (int i = 0; i < nbCartes; i++) {
            joueur.main.ajouterCarte(getCarteDessus());
        }
    }

    public ArrayList<Carte> getCartes() {
        return cartes;
    }

    public void setCartes(ArrayList<Carte> cartes) {
        this.cartes = cartes;
    }
}
