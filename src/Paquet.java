import java.util.Arrays;

public class Paquet {
    Carte[] cartes;

    public Paquet() {
        this.cartes = new Carte[0];
    }

    public Paquet(Carte[] cartes) {
        this.cartes = cartes;
    }

    public void ajouterCarte(Carte carte) {
        // ??
    }

    public Carte getCarteDessus() {
        return this.cartes[this.cartes.length - 1];
    }

    public Carte[] getCartes() {
        return cartes;
    }

    public void setCartes(Carte[] cartes) {
        this.cartes = cartes;
    }
}
