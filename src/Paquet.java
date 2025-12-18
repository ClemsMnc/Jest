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

    public int compterCaractere(Carte.Caractere c) {
        int count = 0;
        for (Carte carte : cartes) {
            if (!carte.isEstJoker() && carte.getCaractere() == c) {
                count++;
            }
        }
        return count;
    }

    public int compterCouleur(Carte.Couleurs couleur) {
        int count = 0;
        for (Carte carte : cartes) {
            if (carte.getCouleur() == couleur) {
                count++;
            }
        }
        return count;
    }

    public Carte getCarteExtremeCouleur(Carte.Couleurs couleur, boolean highest) {
        Carte resultat = null;

        for (Carte c : cartes) {
            if (c.getCouleur() == couleur) {
                if (resultat == null ||
                        (highest && c.getValeurCarte() > resultat.getValeurCarte()) ||
                        (!highest && c.getValeurCarte() < resultat.getValeurCarte())) {
                    resultat = c;
                }
            }
        }
        return resultat;
    }

    public boolean contientJoker() {
        for (Carte c : cartes) {
            if (c.isEstJoker()) return true;
        }
        return false;
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

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cartes: [");
        for (Carte carte : cartes) {
            sb.append(carte.toString()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
