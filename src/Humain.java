import java.util.ArrayList;
import java.util.Scanner;

public class Humain extends Joueur{

    public Humain(String nom) {
        super(nom);
    }

    public Offre faireUneOffre(){
        Scanner scanner = new Scanner(System.in);
        int faceChoisieCachee = 5;
        while( !(faceChoisieCachee == 0  || faceChoisieCachee == 1)){
            System.out.println(this.nom + ":");
            System.out.println(this.main);
            System.out.println("Choisissez la carte qui sera face cachée (0 ou 1)");
            faceChoisieCachee = scanner.nextInt();
            scanner.nextLine();
        }
        System.out.println("ccccccc");
        int faceChoisieVisible = 1 - faceChoisieCachee;
        System.out.println("ffffff");
        return new Offre(main.cartes.get(faceChoisieCachee), main.cartes.get(faceChoisieVisible));
    }

    public void prendreUneOffre(Joueur joueur, boolean faceCachee, ArrayList<Joueur> joueurs){

        Carte prise = joueur.getOffre().prendreCarte(faceCachee);
        if (prise != null) {
            this.jest.ajouterCarte(prise);
        }
        offre.setStatutOffre(false);
    }
}
