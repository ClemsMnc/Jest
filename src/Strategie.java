import java.util.ArrayList;

public interface Strategie {
    public Offre strategieFaireOffre(Joueur joueur);
    public void strategiePrendreOffre(Joueur joueur, ArrayList<Joueur> joueurs);

}
