package modele;

/**
 * Représente une offre dans le jeu Jest.
 * <p>
 * Une offre est composée de deux cartes :
 * - une carte face visible
 * - une carte face cachée
 * <p>
 * Une offre est active tant qu'elle n'a pas été prise.
 */
public class Offre {

    /**
     * modele.Carte face visible de l'offre.
     */
    private Carte carteFaceAvant;

    /**
     * modele.Carte face cachée de l'offre.
     */
    private Carte carteFaceCachee;

    /**
     * Indique si l'offre est encore disponible.
     */
    private boolean statutOffre = true;

    /**
     * Construit une offre à partir de deux cartes.
     *
     * @param carteFaceAvant la carte face visible
     * @param carteFaceCachee la carte face cachée
     */
    public Offre(Carte carteFaceAvant, Carte carteFaceCachee) {
        this.carteFaceAvant = carteFaceAvant;
        this.carteFaceCachee = carteFaceCachee;
    }

    /**
     * Permet de prendre une carte de l'offre.
     * <p>
     * Si la carte face cachée est prise, la carte correspondante
     * est retirée de l'offre. Sinon, la carte face visible est prise.
     *
     * @param faceCachee true si la carte face cachée est prise,
     *                   false si la carte face visible est prise
     * @return la carte prise ou null si aucune carte n'est disponible
     */
    public Carte prendreCarte(boolean faceCachee) {
        Carte res;
        if (faceCachee) {
            res = this.carteFaceCachee;
            setCarteFaceCachee(null);
        } else {
            res = this.carteFaceAvant;
            setCarteFaceAvant(null);
        }
        return res;
    }

    /**
     * Retourne la carte face visible de l'offre.
     *
     * @return la carte face visible
     */
    public Carte getCarteFaceAvant() {
        return carteFaceAvant;
    }

    /**
     * Modifie la carte face visible de l'offre.
     *
     * @param carteFaceAvant la nouvelle carte face visible
     */
    public void setCarteFaceAvant(Carte carteFaceAvant) {
        this.carteFaceAvant = carteFaceAvant;
    }

    /**
     * Retourne la carte face cachée de l'offre.
     *
     * @return la carte face cachée
     */
    public Carte getCarteFaceCachee() {
        return carteFaceCachee;
    }

    /**
     * Modifie la carte face cachée de l'offre.
     *
     * @param carteFaceCachee la nouvelle carte face cachée
     */
    public void setCarteFaceCachee(Carte carteFaceCachee) {
        this.carteFaceCachee = carteFaceCachee;
    }

    /**
     * Indique si l'offre est encore disponible.
     *
     * @return true si l'offre est active, false sinon
     */
    public boolean getStatutOffre() {
        return statutOffre;
    }

    /**
     * Modifie le statut de l'offre.
     *
     * @param statutOffre le nouveau statut de l'offre
     */
    public void setStatutOffre(boolean statutOffre) {
        this.statutOffre = statutOffre;
    }

    /**
     * Retourne une représentation textuelle de l'offre.
     * <p>
     * La carte face cachée n'est jamais affichée.
     *
     * @return une chaîne représentant l'offre
     */
    @Override
    public String toString() {
        return "[carteFaceAvant=" + carteFaceAvant + ", carteFaceCachee = ???]";
    }
}
