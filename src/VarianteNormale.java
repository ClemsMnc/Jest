import java.util.HashMap;
import java.util.Map;

public class VarianteNormale implements VarianteVisitor {

    @Override
    public int visit(Paquet jest) {

        int score = 0;

        boolean hasJoker = false;
        int nbCoeurs = 0;
        int sommeCoeurs = 0;

        Map<Integer, Integer> mapPique = new HashMap<>();
        Map<Integer, Integer> mapTrefle = new HashMap<>();

        // 1. Parcours des cartes
        for (Carte c : jest.getCartes()) {

            if (c.isEstJoker()) {
                hasJoker = true;
                continue;
            }

            int val = valeurCarte(c, jest);

            switch (c.getCouleur()) {

                case Pique:
                    score += val;
                    mapPique.put(val, mapPique.getOrDefault(val, 0) + 1);
                    break;

                case Trefle:
                    score += val;
                    mapTrefle.put(val, mapTrefle.getOrDefault(val, 0) + 1);
                    break;

                case Carreau:
                    score -= val;
                    break;

                case Coeur:
                    nbCoeurs++;
                    sommeCoeurs += val;
                    break;
            }
        }

        // 2. Joker + Cœurs
        if (hasJoker) {
            if (nbCoeurs == 0) {
                score += 4;
            } else if (nbCoeurs >= 1 && nbCoeurs <= 3) {
                score -= sommeCoeurs;
            } else if (nbCoeurs == 4) {
                score += sommeCoeurs;
            }
        }

        // 3. Paires noires (+2 par paire pique + trèfle même valeur)
        for (int v : mapPique.keySet()) {
            int nbP = mapPique.get(v);
            int nbT = mapTrefle.getOrDefault(v, 0);
            score += Math.min(nbP, nbT) * 2;
        }

        return score;
    }

    // Valeur d’une carte (As = 5 s’il est seul de sa couleur)
    private int valeurCarte(Carte c, Paquet jest) {

        int base;
        switch (c.getCaractere()) {
            case Deux: base = 2; break;
            case Trois: base = 3; break;
            case Quatre: base = 4; break;
            case As: base = 1; break;
            default: base = 0;
        }

        if (c.getCaractere() == Carte.Caractere.As) {
            boolean seul = true;
            for (Carte other : jest.getCartes()) {
                if (other != c && !other.isEstJoker()
                        && other.getCouleur() == c.getCouleur()) {
                    seul = false;
                }
            }
            if (seul) base = 5;
        }

        return base;
    }
}
