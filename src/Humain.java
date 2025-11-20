import java.util.Scanner;

public class Humain extends Joueur{

    public Humain(String nom) {
        super(nom);
    }

    public Offre faireUneOffre(){
        Scanner scanner = new Scanner(System.in);
        int faceChoisieCachee = 5;
        while( !(faceChoisieCachee == 0  || faceChoisieCachee == 1)){
            System.out.println(" Choisissez la carte qui sera face cachée (0 ou 1)");
            faceChoisieCachee = Integer.parseInt(scanner.nextLine());
        }
        int faceChoisieVisible = 1 - faceChoisieCachee;
        return new Offre(main.cartes.get(faceChoisieCachee), main.cartes.get(faceChoisieVisible));
    }

    public void prendreUneOffre(Joueur joueur, boolean faceCachee){
    }
}
