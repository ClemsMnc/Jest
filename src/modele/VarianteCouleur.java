package modele;

/**
 * Implémentation de la variante couleur de calcul des scores.
 * <p>
 * Cette variante s'appuie sur la variante normale et ajoute
 * un bonus supplémentaire pour les cartes de couleur noire
 * (Pique et Trèfle).
 */
public class VarianteCouleur implements VarianteVisitor {

    /**
     * Instance de la variante normale utilisée comme base de calcul.
     */
    private final VarianteNormale base = new VarianteNormale();

    /**
     * Calcule le score d'un Jest selon la variante couleur.
     * <p>
     * Le score est d'abord calculé selon la variante normale,
     * puis un point supplémentaire est ajouté pour chaque carte
     * noire (Pique ou Trèfle) présente dans le Jest.
     * <p>
     * Les Jokers ne sont pas pris en compte pour ce bonus.
     *
     * @param jest le paquet représentant le Jest d'un joueur
     * @return le score calculé
     */
    @Override
    public int visit(Paquet jest) {

        int score = base.visit(jest);

        for (Carte c : jest.getCartes()) {
            if (!c.isEstJoker() &&
                    (c.getCouleur() == Carte.Couleurs.Pique ||
                            c.getCouleur() == Carte.Couleurs.Trefle)) {
                score += 1;
            }
        }

        return score;
    }
}
