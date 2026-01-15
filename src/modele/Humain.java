package modele;

import java.util.ArrayList;
import java.util.Scanner;
/**
 * Représente un joueur humain dans le jeu Jest.
 * <p>
 * Un joueur humain interagit avec le jeu via le clavier.
 * Il choisit les cartes constituant son offre et sélectionne
 * les offres des autres joueurs lors de son tour.
 */
public class Humain extends Joueur{
    /**
     * Construit un joueur humain avec un nom donné.
     *
     * @param nom le nom du joueur
     */
    public Humain(String nom) {
        super(nom);
    }
    /**
     * Permet au joueur humain de créer une offre.
     * <p>
     * Le joueur choisit quelle carte de sa main sera face cachée.
     * L'autre carte devient automatiquement la carte visible.
     * Après la création de l'offre, la main du joueur est vidée.
     *
     * @return l'offre créée par le joueur
     */
    @Override
    public Offre faireUneOffre(){
        Scanner scanner = new Scanner(System.in);
        int faceChoisieCachee = 5;
        while( !(faceChoisieCachee == 0  || faceChoisieCachee == 1)){
            System.out.println(this.getNom() + ":");
            System.out.println(this.getMain());
            System.out.println("Choisissez la carte qui sera face cachée (0 ou 1)");
            faceChoisieCachee = scanner.nextInt();
            scanner.nextLine();
        }
        int faceChoisieVisible = 1 - faceChoisieCachee;
        Offre offre =  new Offre(getMain().getCartes().get(faceChoisieVisible), getMain().getCartes().get(faceChoisieCachee));
        this.setMain(new Paquet());
        return offre;
    }
    /**
     * Permet au joueur humain de prendre une carte dans l'offre
     * d'un autre joueur.
     * <p>
     * La carte choisie (visible ou cachée) est ajoutée au Jest
     * du joueur humain, puis l'offre ciblée est fermée.
     *
     * @param joueur le joueur dont l'offre est ciblée
     * @param faceCachee true si la carte face cachée est prise,
     *                   false si la carte face visible est prise
     * @param joueurs la liste des joueurs de la partie
     */
    public void prendreUneOffre(Joueur joueur, boolean faceCachee, ArrayList<Joueur> joueurs){

        Carte prise = joueur.getOffre().prendreCarte(faceCachee);
        if (prise != null) {
            this.getJest().ajouterCarte(prise);
        }
        joueur.getOffre().setStatutOffre(false);

    }
}
