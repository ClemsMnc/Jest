/**
 * Implémentation de la variante Joker de calcul des scores.
 *
 * Cette variante s'appuie sur la variante normale et ajoute
 * un bonus lorsque certaines conditions liées au Joker
 * et aux cartes Coeur sont remplies.
 */
public class VarianteJoker implements VarianteVisitor {

    /**
     * Instance de la variante normale utilisée comme base de calcul.
     */
    private VarianteNormale base = new VarianteNormale();

    /**
     * Calcule le score d'un Jest selon la variante Joker.
     *
     * Le score est d'abord calculé selon la variante normale.
     * Un bonus de 3 points est ensuite accordé si le Jest
     * contient au moins un Joker et au moins deux cartes Coeur.
     *
     * @param jest le paquet représentant le Jest d'un joueur
     * @return le score calculé
     */
    @Override
    public int visit(Paquet jest) {

        int score = base.visit(jest);

        boolean hasJoker = false;
        int nbCoeurs = 0;

        for (Carte c : jest.getCartes()) {
            if (c.isEstJoker()) {
                hasJoker = true;
            } else if (c.getCouleur() == Carte.Couleurs.Coeur) {
                nbCoeurs++;
            }
        }

        if (hasJoker && nbCoeurs >= 2) {
            score += 3;
        }

        return score;
    }
}
