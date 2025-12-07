import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;


public class Strategie1 implements Strategie {
    public Strategie1() {
    }

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
        ArrayList<Joueur> joueursOffresDispo = new ArrayList<>();

        for (Joueur j : joueurs) {

            if (j == joueur) continue;
            Offre o = j.getOffre();

            if (o != null && o.getStatutOffre()) {
                joueursOffresDispo.add(j);
            }
        }

        if (joueursOffresDispo.isEmpty()) return;

        Joueur cible = joueursOffresDispo.get((int) (Math.random() * joueursOffresDispo.size()));
        Offre offreCible = cible.getOffre();

        boolean prendreCachee = Math.random() < 0.5;
        Carte cartePrise = offreCible.prendreCarte(prendreCachee);

        offreCible.setStatutOffre(false);

    }
}

