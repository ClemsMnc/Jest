package modele;

import java.util.ArrayList;

/**
 * Interface représentant une stratégie de jeu pour un joueur ordinateur.
 * <p>
 * Une stratégie définit :
 * - la manière de créer une offre à partir de la main du joueur
 * - la manière de choisir et de prendre une offre parmi celles disponibles
 * <p>
 * Cette interface permet de séparer la logique de décision
 * du fonctionnement général du jeu.
 */
public interface Strategie {

    /**
     * Crée une offre pour le joueur ordinateur.
     * <p>
     * La stratégie choisit quelles cartes de la main du joueur
     * seront placées face visible et face cachée dans l'offre.
     *
     * @param joueur le joueur ordinateur utilisant la stratégie
     * @return l'offre créée
     */
    public Offre strategieFaireOffre(Joueur joueur);

    /**
     * Permet à la stratégie de choisir une offre à prendre.
     * <p>
     * La stratégie sélectionne un joueur cible parmi ceux disposant
     * d'une offre valide, puis décide quelle carte prendre.
     *
     * @param joueur le joueur ordinateur utilisant la stratégie
     * @param joueurs la liste des joueurs disposant d'une offre
     */
    public void strategiePrendreOffre(Joueur joueur, ArrayList<Joueur> joueurs);

}
