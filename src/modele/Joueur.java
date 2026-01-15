package modele;

import java.util.ArrayList;

/**
 * Représente un joueur du jeu Jest.
 * <p>
 * Un joueur possède :
 * - un nom
 * - un score
 * - une main de cartes
 * - un Jest contenant les cartes remportées
 * - une offre courante
 *
 * Cette classe est abstraite et est spécialisée par les joueurs
 * humains et les joueurs ordinateurs.
 */
public abstract class Joueur {

    /**
     * Nom du joueur.
     */
    private String nom;

    /**
     * Score du joueur.
     */
    private Integer score;

    /**
     * modele.Paquet représentant la main du joueur.
     */
    private Paquet main;

    /**
     * modele.Paquet représentant le Jest du joueur.
     */
    private Paquet jest;

    /**
     * modele.Offre courante du joueur.
     */
    private Offre offre;

    /**
     * Construit un joueur avec un nom donné.
     *
     * @param nom le nom du joueur
     */
    public Joueur(String nom) {
        this.nom = nom;
        this.score = 0;
        this.main = new Paquet();
        this.jest = new Paquet();
    }

    /**
     * Permet au joueur de créer une offre à partir de sa main.
     *
     * @return l'offre créée par le joueur
     */
    public abstract Offre faireUneOffre();

    /**
     * Permet au joueur de prendre une carte dans l'offre
     * d'un autre joueur.
     *
     * @param joueur le joueur dont l'offre est ciblée
     * @param faceCachee true si la carte face cachée est prise,
     *                   false si la carte face visible est prise
     * @param joueurs la liste des joueurs de la partie
     */
    public abstract void prendreUneOffre(Joueur joueur, boolean faceCachee, ArrayList<Joueur> joueurs);

    /**
     * Retire et retourne la dernière carte de la main du joueur.
     *
     * @return la dernière carte de la main ou null si la main est vide
     */
    public Carte prendreDerniereCarte() {
        if (main.getCartes().isEmpty()) return null;
        return main.getCartes().removeLast();
    }

    /**
     * Retourne le score du joueur.
     *
     * @return le score du joueur
     */
    public Integer getScore() {
        return score;
    }

    /**
     * Retourne le nom du joueur.
     *
     * @return le nom du joueur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le Jest du joueur.
     *
     * @return le paquet représentant le Jest
     */
    public Paquet getJest() {
        return jest;
    }

    /**
     * Retourne la main du joueur.
     *
     * @return le paquet représentant la main
     */
    public Paquet getMain() {
        return main;
    }

    /**
     * Retourne l'offre courante du joueur.
     *
     * @return l'offre du joueur
     */
    public Offre getOffre() {
        return offre;
    }

    /**
     * Modifie le nom du joueur.
     *
     * @param nom le nouveau nom
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Modifie le score du joueur.
     *
     * @param score le nouveau score
     */
    public void setScore(Integer score) {
        this.score = score;
    }

    /**
     * Modifie la main du joueur.
     *
     * @param main le nouveau paquet représentant la main
     */
    public void setMain(Paquet main) {
        this.main = main;
    }

    /**
     * Modifie le Jest du joueur.
     *
     * @param jest le nouveau paquet représentant le Jest
     */
    public void setJest(Paquet jest) {
        this.jest = jest;
    }

    /**
     * Modifie l'offre courante du joueur.
     *
     * @param offre la nouvelle offre
     */
    public void setOffre(Offre offre) {
        this.offre = offre;
    }
}
