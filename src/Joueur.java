import java.util.ArrayList;

public abstract class Joueur {
    String nom;
    Integer score;
    Paquet main;
    Paquet jest;
    Offre offre;

    public Joueur(String nom) {
        this.nom = nom;
        this.score = 0;
        this.main = new Paquet();
        this.jest = new Paquet();
    }

    public abstract Offre faireUneOffre();

    public abstract void prendreUneOffre(Joueur joueur, boolean faceCachee, ArrayList<Joueur> joueurs);

    public Carte prendreDerniereCarte(){
        if (main.getCartes().isEmpty()) return null;
        return main.getCartes().removeLast();
    }

    public Integer getScore() {
        return score;
    }

    public String getNom() {
        return nom;
    }

    public Paquet getJest() {
        return jest;
    }

    public Paquet getMain() {
        return main;
    }

    public Offre getOffre() {
        return offre;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public void setMain(Paquet main) {
        this.main = main;
    }

    public void setJest(Paquet jest) {
        this.jest = jest;
    }

    public void setOffre(Offre offre) {
        this.offre = offre;
    }
}

