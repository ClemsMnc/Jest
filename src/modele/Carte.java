package modele;

/**
 * Représente une carte du jeu Jest.
 * Une carte est définie par :
 * - un caractère (Deux, Trois, Quatre ou As)
 * - une couleur (Pique, Trèfle, Carreau ou Cœur)
 * - un indicateur précisant si la carte est un Joker
 * - un code de trophée éventuellement associé
 * Le Joker est une carte particulière qui n'a ni caractère
 * ni couleur significatifs et dont la valeur est nulle.
 */
public class Carte {

    /**
     * Indique si la carte est un Joker.
     */
    private boolean estJoker;

    /**
     * Code du trophée associé à la carte.
     */
    private String codeTrophee;

    /**
     * Caractère de la carte.
     */
    private Caractere caractere;

    /**
     * Enumération des caractères possibles des cartes.
     */
    public enum Caractere {
        Deux,
        Trois,
        Quatre,
        As
    }

    /**
     * Couleur de la carte.
     */
    private Couleurs couleur;

    /**
     * Enumération des couleurs possibles des cartes.
     */
    public enum Couleurs {
        Pique,
        Trefle,
        Carreau,
        Coeur
    }

    /**
     * Construit une carte du jeu.
     *
     * @param caractere le caractère de la carte
     * @param couleur la couleur de la carte
     * @param estJoker indique si la carte est un Joker
     * @param codeTrophee le code du trophée associé à la carte
     */
    public Carte(Caractere caractere, Couleurs couleur, boolean estJoker, String codeTrophee) {
        this.caractere = caractere;
        this.couleur = couleur;
        this.estJoker = estJoker;
        this.codeTrophee = codeTrophee;
    }

    /**
     * Retourne le caractère de la carte.
     *
     * @return le caractère de la carte
     */
    public Caractere getCaractere() {
        return caractere;
    }

    /**
     * Modifie le caractère de la carte.
     *
     * @param caractere le nouveau caractère
     */
    public void setCaractere(Caractere caractere) {
        this.caractere = caractere;
    }

    /**
     * Retourne la couleur de la carte.
     *
     * @return la couleur de la carte
     */
    public Couleurs getCouleur() {
        return couleur;
    }

    /**
     * Modifie la couleur de la carte.
     *
     * @param couleur la nouvelle couleur
     */
    public void setCouleur(Couleurs couleur) {
        this.couleur = couleur;
    }

    /**
     * Indique si la carte est un Joker.
     *
     * @return true si la carte est un Joker, false sinon
     */
    public boolean isEstJoker() {
        return estJoker;
    }

    /**
     * Modifie le statut Joker de la carte.
     *
     * @param estJoker true si la carte devient un Joker
     */
    public void setEstJoker(boolean estJoker) {
        this.estJoker = estJoker;
    }

    /**
     * Retourne le code du trophée associé à la carte.
     *
     * @return le code du trophée
     */
    public String getCodeTrophee() {
        return codeTrophee;
    }

    /**
     * Modifie le code du trophée associé à la carte.
     *
     * @param codeTrophee le nouveau code du trophée
     */
    public void setCodeTrophee(String codeTrophee) {
        this.codeTrophee = codeTrophee;
    }

    /**
     * Retourne la valeur numérique de la carte.
     * Le Joker a une valeur de 0.
     *
     * @return la valeur de la carte
     */
    public int getValeurCarte() {
        if (this.estJoker) {
            return 0;
        }
        return switch (this.caractere) {
            case Deux -> 2;
            case Trois -> 3;
            case Quatre -> 4;
            case As -> 1;
        };
    }

    /**
     * Retourne une représentation textuelle de la carte.
     *
     * @return une chaîne représentant la carte
     */
    @Override
    public String toString() {
        if (estJoker) {
            return "Joker";
        } else {
            return caractere.toString() + " de " + couleur.toString();
        }
    }
}
