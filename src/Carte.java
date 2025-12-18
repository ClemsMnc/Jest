public class Carte {
    boolean estJoker;
    String codeTrophee;

    private Caractere caractere;

    public enum Caractere {
        Deux,
        Trois,
        Quatre,
        As
    }

    private Couleurs couleur;

    public enum Couleurs {
        Pique,
        Trefle,
        Carreau,
        Coeur
    }

    public Carte(Caractere caractere, Couleurs couleur, boolean estJoker, String codeTrophee) {
        this.caractere = caractere;
        this.couleur = couleur;
        this.estJoker = estJoker;
        this.codeTrophee = codeTrophee;
    }

    public Caractere getCaractere() {
        return caractere;
    }

    public void setCaractere(Caractere caractere) {
        this.caractere = caractere;
    }

    public Couleurs getCouleur() {
        return couleur;
    }

    public void setCouleur(Couleurs couleur) {
        this.couleur = couleur;
    }

    public boolean isEstJoker() {
        return estJoker;
    }

    public void setEstJoker(boolean estJoker) {
        this.estJoker = estJoker;
    }

    public String getCodeTrophee() {
        return codeTrophee;
    }

    public void setCodeTrophee(String codeTrophee) {
        this.codeTrophee = codeTrophee;
    }

    public int getValeurCarte(){
        if (this.estJoker) {
            return 6;
        }
        return switch (this.caractere) {
            case Deux -> 2;
            case Trois -> 3;
            case Quatre -> 4;
            case As -> 1; // l'as vaut 1 pour la sélection du premier joueur
        };
    }

    public String toString() {
        if (estJoker) {
            return "Joker";
        } else {
            return caractere.toString() + " de " + couleur.toString();
        }
    }
}
