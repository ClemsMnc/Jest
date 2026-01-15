package modele;

import java.util.ArrayList;

/**
 * Implémentation d'une stratégie "intelligente".
 *
 * Cette stratégie évalue les cartes en fonction de leur valeur,
 * de leur couleur et de la composition actuelle du Jest du joueur.
 * Elle cherche à maximiser le gain potentiel lors de la création
 * et de la prise des offres.
 */
public class Strategie2 implements Strategie {

    /**
     * Crée une offre de manière stratégique.
     *
     * La carte ayant le score le plus faible est placée face visible,
     * tandis que la meilleure carte est placée face cachée afin de
     * limiter l'intérêt des adversaires.
     *
     * @param joueur le joueur ordinateur utilisant la stratégie
     * @return l'offre créée
     */
    @Override
    public Offre strategieFaireOffre(Joueur joueur) {

        Carte c1 = joueur.getMain().getCartes().get(0);
        Carte c2 = joueur.getMain().getCartes().get(1);

        Carte visible;
        Carte cachee;

        if (scoreCarte(c1, joueur) < scoreCarte(c2, joueur)) {
            visible = c1;
            cachee  = c2;
        } else {
            visible = c2;
            cachee  = c1;
        }

        joueur.getMain().retirerCarte(visible);
        joueur.getMain().retirerCarte(cachee);

        return new Offre(visible, cachee);
    }

    /**
     * Sélectionne et prend l'offre la plus intéressante disponible.
     * La stratégie évalue les cartes visibles et cachées de chaque offre.
     * Une pénalité est appliquée aux cartes cachées pour représenter
     * l'incertitude associée.
     *
     * @param joueur le joueur ordinateur utilisant la stratégie
     * @param joueurs la liste des joueurs disposant d'une offre
     */
    @Override
    public void strategiePrendreOffre(Joueur joueur, ArrayList<Joueur> joueurs) {

        Joueur meilleureCible = null;
        boolean prendreCachee = false;
        double meilleurScore = -9999;

        for (Joueur j : joueurs) {

            Offre offre = j.getOffre();
            if (offre == null || !offre.getStatutOffre()) continue;

            Carte visible = offre.getCarteFaceAvant();
            Carte cachee = offre.getCarteFaceCachee();

            if (visible != null) {
                double s = scoreCarte(visible, j);
                if (s > meilleurScore) {
                    meilleurScore = s;
                    meilleureCible = j;
                    prendreCachee = false;
                }
            }

            if (cachee != null) {
                double s = scoreCarte(cachee, j) * 0.7;
                if (s > meilleurScore) {
                    meilleurScore = s;
                    meilleureCible = j;
                    prendreCachee = true;
                }
            }
        }

        if (meilleureCible == null) return;

        Offre offreChoisie = meilleureCible.getOffre();

        Carte prise = offreChoisie.prendreCarte(prendreCachee);
        offreChoisie.setStatutOffre(false);

        if (prise != null) {
            joueur.getJest().ajouterCarte(prise);
        }
    }

    /**
     * Calcule un score heuristique pour une carte.
     *
     * Le score dépend :
     * - de la valeur de la carte
     * - de sa couleur
     * - de la composition actuelle du Jest du joueur
     * - de règles spécifiques pour les Jokers
     *
     * @param c la carte à évaluer
     * @param joueur le joueur possédant le Jest analysé
     * @return le score estimé de la carte
     */
    public int scoreCarte(Carte c, Joueur joueur) {

        if (c.isEstJoker()) {
            int coeurs = compterCouleur(joueur, Carte.Couleurs.Coeur);
            if (coeurs == 0) return 10;
            if (coeurs == 1) return 6;
            if (coeurs < 4) return 3;
            return -5;
        }

        int val = scoreValeur(c);

        switch (c.getCouleur()) {
            case Pique:   val += 4; break;
            case Trefle:  val += 3; break;
            case Carreau: val -= 2; break;
            case Coeur:   val -= 1; break;
        }

        if (c.getCouleur() == Carte.Couleurs.Pique ||
                c.getCouleur() == Carte.Couleurs.Trefle) {

            if (possedeCouleurEtValeur(joueur, c)) {
                val += 3;
            }
        }

        return val;
    }

    /**
     * Retourne la valeur de base d'une carte selon son caractère.
     *
     * @param c la carte à évaluer
     * @return la valeur associée au caractère
     */
    public int scoreValeur(Carte c) {
        switch (c.getCaractere()) {
            case As:     return 1;
            case Deux:   return 2;
            case Trois:  return 3;
            case Quatre: return 4;
        }
        return 0;
    }

    /**
     * Compte le nombre de cartes d'une couleur donnée
     * dans le Jest du joueur.
     *
     * @param joueur le joueur concerné
     * @param couleur la couleur recherchée
     * @return le nombre de cartes de cette couleur
     */
    private int compterCouleur(Joueur joueur, Carte.Couleurs couleur) {
        int n = 0;
        for (Carte c : joueur.getJest().getCartes()) {
            if (c.getCouleur() == couleur) {
                n++;
            }
        }
        return n;
    }

    /**
     * Indique si le joueur possède déjà une carte
     * de même caractère et de couleur noire (Pique ou Trèfle).
     *
     * @param joueur le joueur concerné
     * @param carte la carte à comparer
     * @return true si une telle carte est présente, false sinon
     */
    private boolean possedeCouleurEtValeur(Joueur joueur, Carte carte) {
        for (Carte c : joueur.getJest().getCartes()) {
            if (c.getCaractere() == carte.getCaractere()) {
                if (c.getCouleur() == Carte.Couleurs.Pique ||
                        c.getCouleur() == Carte.Couleurs.Trefle)
                    return true;
            }
        }
        return false;
    }
}
