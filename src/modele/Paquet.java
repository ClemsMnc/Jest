package modele;

import java.util.ArrayList;

/**
 * Représente un paquet de cartes dans le jeu Jest.
 *
 * Un paquet est une collection de cartes utilisée pour représenter
 * la pioche, la main d'un joueur ou son Jest.
 * Cette classe fournit des méthodes utilitaires pour manipuler
 * et analyser les cartes qu'elle contient.
 */
public class Paquet {

    /**
     * Liste des cartes contenues dans le paquet.
     */
    private ArrayList<Carte> cartes;

    /**
     * Construit un paquet vide.
     */
    public Paquet() {
        this.cartes = new ArrayList<Carte>();
    }

    /**
     * Construit un paquet à partir d'une liste de cartes existante.
     *
     * @param cartes la liste de cartes à utiliser
     */
    public Paquet(ArrayList<Carte> cartes) {
        this.cartes = cartes;
    }

    /**
     * Mélange aléatoirement les cartes du paquet.
     */
    public void melanger() {
        java.util.Collections.shuffle(this.cartes);
    }

    /**
     * Ajoute une carte au paquet.
     *
     * @param carte la carte à ajouter
     */
    public void ajouterCarte(Carte carte) {
        this.cartes.add(carte);
    }

    /**
     * Compte le nombre de cartes d'un caractère donné dans le paquet.
     * Les Jokers ne sont pas pris en compte.
     *
     * @param c le caractère à compter
     * @return le nombre de cartes de ce caractère
     */
    public int compterCaractere(Carte.Caractere c) {
        int count = 0;
        for (Carte carte : cartes) {
            if (!carte.isEstJoker() && carte.getCaractere() == c) {
                count++;
            }
        }
        return count;
    }

    /**
     * Compte le nombre de cartes d'une couleur donnée dans le paquet.
     *
     * @param couleur la couleur à compter
     * @return le nombre de cartes de cette couleur
     */
    public int compterCouleur(Carte.Couleurs couleur) {
        int count = 0;
        for (Carte carte : cartes) {
            if (carte.getCouleur() == couleur) {
                count++;
            }
        }
        return count;
    }

    /**
     * Retourne la carte extrême d'une couleur donnée.
     *
     * Si highest est true, retourne la carte de valeur maximale.
     * Sinon, retourne la carte de valeur minimale.
     *
     * @param couleur la couleur recherchée
     * @param highest true pour la valeur maximale, false pour la minimale
     * @return la carte correspondante ou null si aucune n'est trouvée
     */
    public Carte getCarteExtremeCouleur(Carte.Couleurs couleur, boolean highest) {
        Carte resultat = null;

        for (Carte c : cartes) {
            if (c.getCouleur() == couleur) {
                if (resultat == null ||
                        (highest && c.getValeurCarte() > resultat.getValeurCarte()) ||
                        (!highest && c.getValeurCarte() < resultat.getValeurCarte())) {
                    resultat = c;
                }
            }
        }
        return resultat;
    }

    /**
     * Indique si le paquet contient au moins un Joker.
     *
     * @return true si le paquet contient un Joker, false sinon
     */
    public boolean contientJoker() {
        for (Carte c : cartes) {
            if (c.isEstJoker()) return true;
        }
        return false;
    }

    /**
     * Retire une carte spécifique du paquet.
     *
     * @param carte la carte à retirer
     */
    public void retirerCarte(Carte carte) {
        this.cartes.remove(carte);
    }

    /**
     * Retire et retourne la carte située sur le dessus du paquet.
     *
     * @return la carte retirée
     */
    public Carte getCarteDessus() {
        return this.cartes.removeLast();
    }

    /**
     * Distribue un nombre donné de cartes à un joueur.
     *
     * Les cartes sont retirées du paquet courant et ajoutées
     * à la main du joueur.
     *
     * @param joueur le joueur recevant les cartes
     * @param nbCartes le nombre de cartes à distribuer
     */
    public void distribuer(Joueur joueur, int nbCartes) {
        for (int i = 0; i < nbCartes; i++) {
            joueur.getMain().ajouterCarte(getCarteDessus());
        }
    }

    /**
     * Retourne la liste des cartes du paquet.
     *
     * @return la liste des cartes
     */
    public ArrayList<Carte> getCartes() {
        return cartes;
    }

    /**
     * Modifie la liste des cartes du paquet.
     *
     * @param cartes la nouvelle liste de cartes
     */
    public void setCartes(ArrayList<Carte> cartes) {
        this.cartes = cartes;
    }

    /**
     * Retourne une représentation textuelle du paquet.
     *
     * @return une chaîne représentant le contenu du paquet
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Cartes: [");
        for (Carte carte : cartes) {
            sb.append(carte.toString()).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append("]");
        return sb.toString();
    }
}
