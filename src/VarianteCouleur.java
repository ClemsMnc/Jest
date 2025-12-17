public class VarianteCouleur implements VarianteVisitor {

    private VarianteNormale base = new VarianteNormale();

    @Override
    public int calculerScore(Paquet jest) {

        int score = base.calculerScore(jest);

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
