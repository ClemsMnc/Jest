public class VarianteCouleur implements VarianteVisitor {

    private VarianteNormale base = new VarianteNormale();

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
