package modele;

import java.util.HashMap;
import java.util.Map;

/**
 * Implémentation de la variante normale de calcul des scores.
 * <p>
 * Cette variante applique les règles standards du jeu Jest :
 * - les cartes Pique et Trèfle rapportent des points
 * - les cartes Carreau font perdre des points
 * - les cartes Coeur interagissent avec la présence d'un Joker
 * - des bonus sont accordés pour les paires noires (Pique / Trèfle)
 */
public class VarianteNormale implements VarianteVisitor {

    /**
     * Calcule le score d'un Jest selon la variante normale.
     * <p>
     * Le calcul prend en compte :
     * - la couleur des cartes
     * - la valeur des cartes
     * - la présence d'un Joker
     * - les interactions avec les cartes Coeur
     * - les paires noires de même valeur
     *
     * @param jest le paquet représentant le Jest d'un joueur
     * @return le score calculé
     */
    @Override
    public int visit(Paquet jest) {

        int score = 0;

        boolean hasJoker = false;
        int nbCoeurs = 0;
        int sommeCoeursBrute = 0;

        Map<Integer, Integer> pique = new HashMap<>();
        Map<Integer, Integer> trefle = new HashMap<>();

        for (Carte c : jest.getCartes()) {

            if (c.isEstJoker()) {
                hasJoker = true;
                continue;
            }

            int face = valeurFace(c);

            switch (c.getCouleur()) {

                case Pique -> {
                    int val = valeurNormale(c, jest);
                    score += val;
                    pique.put(face, pique.getOrDefault(face, 0) + 1);
                }

                case Trefle -> {
                    int val = valeurNormale(c, jest);
                    score += val;
                    trefle.put(face, trefle.getOrDefault(face, 0) + 1);
                }

                case Carreau -> {
                    int val = valeurNormale(c, jest);
                    score -= val;
                }

                case Coeur -> {
                    nbCoeurs++;
                    sommeCoeursBrute += face;
                }
            }
        }

        if (hasJoker) {
            if (nbCoeurs == 0) {
                score += 4;
            } else if (nbCoeurs <= 3) {
                score -= sommeCoeursBrute;
            } else {
                score += sommeCoeursBrute;
            }
        }

        for (int v : pique.keySet()) {
            score += Math.min(pique.get(v), trefle.getOrDefault(v, 0)) * 2;
        }

        return score;
    }

    /**
     * Retourne la valeur brute d'une carte selon son caractère.
     *
     * @param c la carte à évaluer
     * @return la valeur associée au caractère
     */
    private int valeurFace(Carte c) {
        return switch (c.getCaractere()) {
            case Deux -> 2;
            case Trois -> 3;
            case Quatre -> 4;
            case As -> 1;
            default -> 0;
        };
    }

    /**
     * Calcule la valeur normale d'une carte.
     * <p>
     * Les As valent :
     * - 5 points s'ils sont seuls de leur couleur
     * - 1 point sinon
     * <p>
     * Les autres cartes conservent leur valeur brute.
     *
     * @param c la carte à évaluer
     * @param jest le Jest dans lequel se trouve la carte
     * @return la valeur effective de la carte
     */
    private int valeurNormale(Carte c, Paquet jest) {
        if (c.getCaractere() != Carte.Caractere.As) {
            return valeurFace(c);
        }

        for (Carte other : jest.getCartes()) {
            if (other != c && !other.isEstJoker()
                    && other.getCouleur() == c.getCouleur()) {
                return 1;
            }
        }
        return 5;
    }
}
