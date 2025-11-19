public class Carte {
    char caractere;
    char couleur;
    boolean estJoker;
    String codeTrophee;

    public Carte(char caracter, char couleur, boolean estJoker, String codeTrophee) {
        this.caractere = caracter;
        this.couleur = couleur;
        this.estJoker = estJoker;
        this.codeTrophee = codeTrophee;
    }
}
