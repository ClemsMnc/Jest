public class Carte {
    char caractere;
    char couleur;
    boolean estJoker;
    String codeTrophee;

    public Carte(char caractere, char couleur, boolean estJoker, String codeTrophee) {
        this.caractere = caractere;
        this.couleur = couleur;
        this.estJoker = estJoker;
        this.codeTrophee = codeTrophee;
    }

    public char getCaractere() {
        return caractere;
    }

    public void setCaractere(char caractere) {
        this.caractere = caractere;
    }

    public char getCouleur() {
        return couleur;
    }

    public void setCouleur(char couleur) {
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
