import java.util.HashMap;
import java.util.Map;

public class VarianteNormale implements VarianteVisitor {

    @Override
    public int visit(Paquet jest) {

        int score = 0;

        boolean hasJoker = false;
        int nbCoeurs = 0;
        int sommeCoeursBrute = 0; // As = 1 ici

        Map<Integer, Integer> pique = new HashMap<>();
        Map<Integer, Integer> trefle = new HashMap<>();

        // 1ère passe : analyse
        for (Carte c : jest.getCartes()) {

            if (c.isEstJoker()) {
                hasJoker = true;
                continue;
            }

            int face = valeurFace(c); // As = 1

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
                    sommeCoeursBrute += face; // IMPORTANT
                }
            }
        }

        // Joker + Cœurs
        if (hasJoker) {
            if (nbCoeurs == 0) {
                score += 4;
            } else if (nbCoeurs <= 3) {
                score -= sommeCoeursBrute;
            } else {
                score += sommeCoeursBrute;
            }
        }

        // Paires noires
        for (int v : pique.keySet()) {
            score += Math.min(pique.get(v), trefle.getOrDefault(v, 0)) * 2;
        }

        return score;
    }

    private int valeurFace(Carte c) {
        return switch (c.getCaractere()) {
            case Deux -> 2;
            case Trois -> 3;
            case Quatre -> 4;
            case As -> 1;
            default -> 0;
        };
    }

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