import java.util.Scanner;

public class Strategie1 implements Strategie {
    public Strategie1() {
    }

    public class Strategie1 implements Strategie {

        @Override
        public Offre strategieFaireOffre(Joueur joueur) {
            // on récupère la main du joueur
            Paquet main = joueur.getMain();

            int nombre = (int)(Math.random() * 2);
            int faceChoisieVisible = 1 - nombre;

            return new Offre(
                    main.getCartes().get(nombre),
                    main.getCartes().get(faceChoisieVisible)
            );
        }

        @Override
        public void strategiePrendreOffre(Joueur joueur) {
            //TODO
        }
    }}
