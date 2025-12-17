public interface VarianteVisitor {

    /**
     * Calcule le score du Jest passé en paramètre.
     * @param jest paquet représentant le Jest du joueur (cartes + trophées).
     * @return score entier.
     */
    public abstract int calculerScore(Paquet jest);

    /**
     * Implémente les règles « normales » de jest :
     * Piques & trèfles : + valeur
     * Carreaux : - valeur
     * Cœurs : 0 sans Joker, ou modifiés si joker présent
     * Joker & cœurs : règles spéciales
     * As : vaut 5 si seul de sa couleur, sinon 1
     * Paires noires (pique + trèfle même valeur) : +2 par paire
     */
    public int calculScoreDeBase(Paquet jest) {
        java.util.List<Carte> piques = new java.util.ArrayList<>();
        java.util.List<Carte> trefles = new java.util.ArrayList<>();
        java.util.List<Carte> carreaux = new java.util.ArrayList<>();
        java.util.List<Carte> coeurs = new java.util.ArrayList<>();

        boolean hasJoker = false;

        // Séparer les cartes par couleur
        for (Carte c : jest.getCartes()) {
            if (c.isEstJoker()) {
                hasJoker = true;
            } else {
                switch (c.getCouleur()) {
                    case Pique -> piques.add(c);
                    case Trefle -> trefles.add(c);
                    case Carreau -> carreaux.add(c);
                    case Coeur -> coeurs.add(c);
                }
            }
        }

        int nbPiques = piques.size();
        int nbTrefles = trefles.size();
        int nbCarreaux = carreaux.size();
        int nbCoeurs = coeurs.size();

        // Map valeur -> nb de cartes pour les noirs (pour les paires)
        java.util.Map<Integer, Integer> mapPique = new java.util.HashMap<>();
        java.util.Map<Integer, Integer> mapTrefle = new java.util.HashMap<>();

        int score = 0;

        // Piques : + valeur
        for (Carte c : piques) {
            int val = valeurCarte(c, nbPiques);
            score = score + val;
            mapPique.put(val, mapPique.getOrDefault(val, 0) + 1);
        }

        //  Trèfles : + valeur
        for (Carte c : trefles) {
            int val = valeurCarte(c, nbTrefles);
            score += val;
            mapTrefle.put(val, mapTrefle.getOrDefault(val, 0) + 1);
);
        }

        //  Carreaux : - valeur
        for (Carte c : carreaux) {
            int val = valeurCarte(c, nbCarreaux);
            score = score - val;
        }

        //  Cœurs + Joker
        int sommeCoeurs = 0;
        for (Carte c : coeurs) {
            sommeCoeurs = sommeCoeurs + valeurCarte(c, nbCoeurs);
        }

        if (!hasJoker) {
            // Cœurs valent 0, Joker absent
            // rien à ajouter
        } else {
            if (nbCoeurs == 0) {
                // Joker +4
                score += 4;
            } else if (nbCoeurs >= 1 && nbCoeurs <= 3) {
                // Joker 0, Cœurs négatifs
                score -= sommeCoeurs;
            } else if (nbCoeurs == 4) {
                // Joker 0, Cœurs positifs
                score += sommeCoeurs;
            }
        }

        //  Paires noires (Pique + Trèfle même valeur) : +2 par paire
        for (int v = 1; v <= 5; v++) {
            int nbP = mapPique.getOrDefault(v, 0);
            int nbT = mapTrefle.getOrDefault(v, 0);
            int nbPaires = Math.min(nbP, nbT);
            score += nbPaires * 2;
        }

        return score;
    }

    /**
     * Convertit un caractère de carte en valeur numérique en appliquant
     * la règle : As vaut 5 s'il est seul dans sa couleur, sinon 1.
     * @param carte carte à évaluer
     * @param nbCartesCouleur nombre total de cartes de cette couleur dans le Jest
     */
    public int valeurCarte(Carte carte, int nbCartesCouleur) {
        if (carte.getCaractere() == null) {
            return 0;
        }
        switch (carte.getCaractere()) {
            case Deux -> {
                return 2;
            }
            case Trois -> {
                return 3;
            }
            case Quatre -> {
                return 4;
            }
            case As -> {
                // As tout seul dans sa couleur => 5, sinon 1
                return (nbCartesCouleur == 1) ? 5 : 1;
            }
            default -> {
                return 0;
            }
        }
    }
}
