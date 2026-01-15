package modele;

/**
 * Interface représentant une variante de calcul des scores.
 *
 * Cette interface suit le principe du patron de conception Visitor.
 * Elle permet de définir différentes règles de calcul des scores
 * appliquées au Jest d'un joueur sans modifier la structure du jeu.
 */
public interface VarianteVisitor {

    /**
     * Calcule le score d'un Jest selon une variante donnée.
     *
     * @param jest le paquet représentant le Jest d'un joueur
     * @return le score calculé pour ce Jest
     */
    int visit(Paquet jest);
}
