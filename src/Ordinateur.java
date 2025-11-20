public class Ordinateur extends Joueur{

    private Strategie strategie;

    public Ordinateur(String nom) {
        super(nom);
    }


    public Offre faireUneOffre(){
        // TODO
        return null;

    }

    public void prendreUneOffre(Joueur joueur, boolean faceCachee){
    }

    public Strategie getStrategie() {
        return strategie;
    }

    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }
}
