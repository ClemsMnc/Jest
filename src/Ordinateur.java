import java.util.Scanner;

public class Ordinateur extends Joueur{

    private Strategie strategie;

    public Ordinateur(String nom) {

        super(nom);
    }


    public Offre faireUneOffre(){
        return this.strategie.strategieFaireOffre(this);
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
