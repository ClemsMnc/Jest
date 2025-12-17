import java.util.ArrayList;

public interface Strategie {
    public Offre strategieFaireOffre(Joueur joueur);
    public void strategiePrendreOffre(ArrayList<Joueur> joueurs);

}
