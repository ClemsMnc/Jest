import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;


public class Strategie1 implements Strategie {

    public Strategie1() {}

    @Override
    public Offre strategieFaireOffre(Joueur joueur) {

        Paquet main = joueur.getMain();

        int faceCachee = (int) (Math.random() * 2);
        int faceVisible = 1 - faceCachee;

        Carte cachee = main.getCartes().get(faceCachee);
        Carte visible = main.getCartes().get(faceVisible);

        main.retirerCarte(cachee);
        main.retirerCarte(visible);

        return new Offre(visible, cachee);
    }


    @Override
    public void strategiePrendreOffre(Joueur joueur, ArrayList<Joueur> joueurs) {

        Joueur cible = joueurs.get((int) (Math.random() * joueurs.size()));
        Offre offreCible = cible.getOffre();

        boolean prendreCachee = Math.random() < 0.5;
        Carte cartePrise = offreCible.prendreCarte(prendreCachee);

        if (cartePrise != null) {
            joueur.getJest().ajouterCarte(cartePrise); // 🔥 ENFIN AU BON ENDROIT
        }

        offreCible.setStatutOffre(false);


    }
}

