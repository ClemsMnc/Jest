public class VarianteJoker implements VarianteVisitor {

    private VarianteNormale base = new VarianteNormale();

    @Override
    public int calculerScore(Paquet jest) {

        int score = base.calculerScore(jest);

        boolean hasJoker = false;
        int nbCoeurs = 0;

        for (Carte c : jest.getCartes()) {
            if (c.isEstJoker()) hasJoker = true;
            else if (c.getCouleur() == Carte.Couleurs.Coeur) nbCoeurs++;
        }

        if (hasJoker && nbCoeurs >= 2) {
            score += 3;
        }

        return score;
    }
}
