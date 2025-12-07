import java.util.ArrayList;

public class Partie {
    ArrayList<Carte> trophees = new ArrayList<>();
    ArrayList<Joueur> joueurs = new ArrayList<>();
    ArrayList<Carte> cartes = new ArrayList<>();
    ArrayList<Carte> paquetManche = new ArrayList<>();

    public static void main(String[] args) {

        Partie partie = new Partie();

        partie.configurerPartie();
        partie.jouerPartie();
    }

    public Partie() {}

    public Paquet creerPaquetBase(){
        ArrayList<Carte> cartes = new ArrayList<>();
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Carreau,false,"M4"));
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Coeur,false,"J"));
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Pique,false,"HT"));
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Trefle,false,"HP"));

        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Carreau,false,"HC"));
        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Coeur,false,"J"));
        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Pique,false,"NB3"));
        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Trefle,false,"LC"));

        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Carreau,false,"LC"));
        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Coeur,false,"J"));
        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Pique,false,"NB2"));
        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Trefle,false,"HC"));

        cartes.add(new Carte(Carte.Caractere.Quatre, Carte.Couleurs.Carreau,false,"BJNJ"));
        cartes.add(new Carte(Carte.Caractere.Quatre, Carte.Couleurs.Coeur,false,"J"));
        cartes.add(new Carte(Carte.Caractere.Quatre, Carte.Couleurs.Pique,false,"LT"));
        cartes.add(new Carte(Carte.Caractere.Quatre, Carte.Couleurs.Trefle,false,"LP"));

        cartes.add(new Carte(null, null,true,"BJ"));

        return new Paquet(cartes);
    }

    public ArrayList<Carte> getTrophees() {
        return trophees;
    }

    public ArrayList<Joueur> getJoueurs() {
        return joueurs;
    }

    public ArrayList<Carte> getCartes() {
        return cartes;
    }

    public ArrayList<Carte> getPaquetManche() {
        return paquetManche;
    }

    public void setTrophees(ArrayList<Carte> trophees) {
        this.trophees = trophees;
    }

    public void setJoueurs(ArrayList<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public void setCartes(ArrayList<Carte> cartes) {
        this.cartes = cartes;
    }

    public void setPaquetManche(ArrayList<Carte> paquetManche) {
        this.paquetManche = paquetManche;
    }

    public void configurerPartie() {

    }

    public void jouerPartie() {

    }

    public void sauvegarderPartie(String nomFichier) {
    }

    public void chargerPartie(String cheminAccesFichier){

    }

    public void accept(){

    }

    public boolean estTerminee(){
        return false;
    }

    public void distribuerTrophees(){

    }

    public void determinerPremierJoueur(){

    }

    public void constituerPaquetManche(){

    }

    public void selectionnerOffres(){

    }

    public boolean encoreDesOffresDispo(){return true;
    }
}
