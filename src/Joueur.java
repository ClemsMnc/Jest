public class Joueur {
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

    public void faireUneOffre(){}

    public void prendreUneOffre(Joueur joueur, boolean faceCachee){}
}
