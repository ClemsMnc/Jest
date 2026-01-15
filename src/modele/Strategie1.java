package modele;

import java.util.ArrayList;

/**
 * Implémentation d'une stratégie aléatoire.
 * <p>
 * Cette stratégie prend toutes ses décisions de manière aléatoire :
 * - les cartes choisies pour créer une offre
 * - l'offre à prendre
 * - la carte prise (face visible ou face cachée)
 */
public class Strategie1 implements Strategie {

    /**
     * Construit une stratégie aléatoire.
     */
    public Strategie1() {}

    /**
     * Crée une offre de manière aléatoire.
     * <p>
     * Deux cartes sont choisies dans la main du joueur :
     * l'une devient face visible, l'autre face cachée.
     *
     * @param joueur le joueur ordinateur utilisant la stratégie
     * @return l'offre créée
     */
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

    /**
     * Prend une offre de manière aléatoire.
     * <p>
     * La stratégie choisit aléatoirement :
     * - un joueur cible parmi ceux disposant d'une offre
     * - la carte à prendre (face visible ou face cachée)
     * <p>
     * La carte prise est ajoutée au Jest du joueur.
     *
     * @param joueur le joueur ordinateur utilisant la stratégie
     * @param joueurs la liste des joueurs disposant d'une offre
     */
    @Override
    public void strategiePrendreOffre(Joueur joueur, ArrayList<Joueur> joueurs) {

        Joueur cible = joueurs.get((int) (Math.random() * joueurs.size()));
        Offre offreCible = cible.getOffre();

        boolean prendreCachee = Math.random() < 0.5;
        Carte cartePrise = offreCible.prendreCarte(prendreCachee);

        if (cartePrise != null) {
            joueur.getJest().ajouterCarte(cartePrise);
        }

        offreCible.setStatutOffre(false);
    }
}
