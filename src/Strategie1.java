import java.util.Scanner;

public class Strategie1 implements Strategie {
    public Strategie1() {
    }

    public Offre strategieFaireOffre(){
        int nombre = (int)(Math.random()*2);
        int faceChoisieVisible = 1 -nombre;
        return new Offre(main.cartes.get(nombre), main.cartes.get(faceChoisieVisible));
    }

    public void strategiePrendreOffre(){
    }
}
