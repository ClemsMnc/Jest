import java.util.HashMap;
import java.util.Map;

public class VarianteNormale implements VarianteVisitor {

    @Override
    public int visit(Paquet jest) {

        int score = 0;

        boolean hasJoker = false;
        int nbCoeurs = 0;
        int sommeCoeursFace = 0; // As = 1 ici

        Map<Integer, Integer> pique = new HashMap<>();
        Map<Integer, Integer> trefle = new HashMap<>();

        // Parcours des cartes
        for (Carte c : jest.getCartes()) {

            if (c.isEstJoker()) {
                hasJoker = true;
                continue;
            }

            int face = valeurFace(c); // As = 1
            int valeurNormale = valeurNormale(c, jest);

            switch (c.getCouleur()) {

                case Pique -> {
                    score += valeurNormale;
                    pique.put(face, pique.getOrDefault(face, 0) + 1);
                }

                case Trefle -> {
                    score += valeurNormale;
                    trefle.put(face, trefle.getOrDefault(face, 0) + 1);
                }

                case Carreau -> {
                    score -= valeurNormale;
                }

                case Coeur -> {
                    nbCoeurs++;
                    sommeCoeursFace += face;
                }
            }
        }

        // Joker + Coeurs
        if (hasJoker) {
            if (nbCoeurs == 0) {
                score += 4;
            } else if (nbCoeurs <= 3) {
                score -= sommeCoeursFace;
            } else {
                score += sommeCoeursFace;
            }
        }

        // Paires noires
        for (int v : pique.keySet()) {
            score += Math.min(pique.get(v), trefle.getOrDefault(v, 0)) * 2;
        }

        return score;
    }

    // Valeur face (As = 1)
    private int valeurFace(Carte c) {
        return switch (c.getCaractere()) {
            case Deux -> 2;
            case Trois -> 3;
            case Quatre -> 4;
            case As -> 1;
        };
    }

    // Valeur normale (As = 5 s'il est seul de sa couleur)
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
