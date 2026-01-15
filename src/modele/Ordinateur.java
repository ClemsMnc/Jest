package modele;

import java.util.ArrayList;

/**
 * Représente un joueur ordinateur dans le jeu Jest.
 *
 * Un joueur ordinateur ne prend pas de décisions via des entrées clavier.
 * Toutes ses actions (création et prise d'offres) sont déléguées à une
 * stratégie associée.
 */
public class Ordinateur extends Joueur {

    /**
     * Stratégie utilisée par le joueur ordinateur pour prendre ses décisions.
     */
    private Strategie strategie;

    /**
     * Construit un joueur ordinateur avec un nom donné.
     *
     * @param nom le nom du joueur
     */
    public Ordinateur(String nom) {
        super(nom);
    }

    /**
     * Permet au joueur ordinateur de créer une offre.
     *
     * La création de l'offre est entièrement déléguée à la stratégie
     * associée au joueur.
     *
     * @return l'offre créée par la stratégie
     */
    @Override
    public Offre faireUneOffre() {
        return this.strategie.strategieFaireOffre(this);
    }

    /**
     * Permet au joueur ordinateur de prendre une offre.
     *
     * Le choix de l'offre et de la carte à prendre est entièrement
     * délégué à la stratégie associée au joueur.
     *
     * @param joueur paramètre non utilisé dans cette implémentation
     * @param faceCachee paramètre non utilisé dans cette implémentation
     * @param joueurs la liste des joueurs disposant d'une offre
     */
    @Override
    public void prendreUneOffre(Joueur joueur, boolean faceCachee, ArrayList<Joueur> joueurs) {
        this.strategie.strategiePrendreOffre(this, joueurs);
    }

    /**
     * Retourne la stratégie associée au joueur ordinateur.
     *
     * @return la stratégie du joueur
     */
    public Strategie getStrategie() {
        return strategie;
    }

    /**
     * Associe une stratégie au joueur ordinateur.
     *
     * @param strategie la stratégie à utiliser
     */
    public void setStrategie(Strategie strategie) {
        this.strategie = strategie;
    }
}
