public class Carte {
    boolean estJoker;
    String codeTrophee;

    private Caractere caractere;

    public enum Caractere {
        Deux,
        Trois,
        Quatre,
        As;
    }

    private Couleurs couleur;

    public enum Couleurs {
        Carreau,
        Coeur,
        Trefle,
        Pique;
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
}
