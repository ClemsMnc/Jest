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

    public abstract void faireUneOffre();

    public abstract void prendreUneOffre(Joueur joueur, boolean faceCachee);
}
