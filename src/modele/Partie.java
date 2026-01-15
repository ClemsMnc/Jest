package modele;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
/**
 * Représente une partie complète du jeu Jest.
 * Cette classe gère :
 * - la configuration de la partie (joueurs, cartes, trophées, variante)
 * - le déroulement des manches
 * - la sélection des offres
 * - la sauvegarde et le chargement d'une partie
 * - l'attribution des trophées
 * - le calcul et l'affichage des scores finaux
 */
public class Partie {
    /**
     * Liste des trophées sélectionnés pour la partie.
     */
    private ArrayList<Carte> trophees = new ArrayList<>();

    /**
     * Liste des joueurs participant à la partie.
     */
    private ArrayList<Joueur> joueurs = new ArrayList<>();

    /**
     * Paquet principal de cartes (pioche).
     */
    private Paquet cartes = new Paquet();

    /**
     * Paquet utilisé pour une manche.
     */
    private Paquet paquetManche = new Paquet();

    /**
     * Variante utilisée pour le calcul des scores.
     */
    private VarianteVisitor variante = null;

    /**
     * Liste des codes de trophées existants.
     */
    private final String[] codesTropheesExistants = {
            "M2","M3","M4","MA","J","HT","HP","HCA","HCO",
            "NB2","NB3","NB4","NBA","LCA","LCO","LT","LP","BJNJ","BJ"
    };


    /**
     * Point d'entrée du programme.
     *
     * Permet à l'utilisateur de lancer une nouvelle partie
     * ou de charger une partie sauvegardée.
     *
     * @param args arguments de la ligne de commande
     */

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        Partie partie = new Partie();

        System.out.println("=== JEST ===");
        System.out.println("1 - Nouvelle partie");
        System.out.println("2 - Charger une partie");
        System.out.print("Votre choix : ");

        int choix = sc.nextInt();
        sc.nextLine();
        if (choix == 2) {
            System.out.print("Nom du fichier de sauvegarde : ");
            String fichier = sc.nextLine();
            partie.chargerPartie(fichier);
            System.out.println("modele.Partie chargée avec succès !");
        } else {
            partie.configurerPartie();
        }

        partie.jouerPartie();
    }

    public Partie() {}
    /**
     * Crée le paquet de base du jeu Jest.
     * <p>
     * Le paquet contient les cartes "classiques" du jeu ainsi qu'un Joker.
     * Chaque carte est associée à un code de trophée.
     *
     * @return un paquet contenant les cartes de base
     */
    public Paquet creerPaquetBase(){
        ArrayList<Carte> cartes = new ArrayList<>();
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Carreau,false,"M4"));
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Coeur,false,"J"));
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Pique,false,"HT"));
        cartes.add(new Carte(Carte.Caractere.As, Carte.Couleurs.Trefle,false,"HP"));

        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Carreau,false,"HC"));
        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Coeur,false,"J"));
        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Pique,false,"NB3"));
        cartes.add(new Carte(Carte.Caractere.Deux, Carte.Couleurs.Trefle,false,"LCO"));

        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Carreau,false,"LCA"));
        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Coeur,false,"J"));
        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Pique,false,"NB2"));
        cartes.add(new Carte(Carte.Caractere.Trois, Carte.Couleurs.Trefle,false,"HCO"));

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

    public Paquet getCartes() {
        return cartes;
    }

    public Paquet getPaquetManche() {
        return paquetManche;
    }

    public void setTrophees(ArrayList<Carte> trophees) {
        this.trophees = trophees;
    }

    public void setJoueurs(ArrayList<Joueur> joueurs) {
        this.joueurs = joueurs;
    }

    public void setCartes(Paquet cartes) {
        this.cartes = cartes;
    }

    public void setPaquetManche(Paquet paquetManche) {
        this.paquetManche = paquetManche;
    }
    /**
     * Configure entièrement la partie.
     */
    public void configurerPartie() {
        System.out.println("Configuration de la partie");
        System.out.println("-------------------------");
        System.out.println("Configuration des joueurs");
        configurerJoueurs();
        System.out.println("-------------------------");
        System.out.println("Configuration des cartes");
        configurerCartes();
        System.out.println("-------------------------");
        System.out.println("Configuration de la variante");
        configurerVariante();
        System.out.println("-------------------------");
        System.out.println("Configuration des trophées");
        configurerTrophees();
    }
    /**
     * Sélectionne aléatoirement les trophées de la partie.
     */
    public void configurerTrophees() {
        this.cartes.melanger();
        this.trophees.add(this.cartes.getCarteDessus());
        this.trophees.add(this.cartes.getCarteDessus());
        System.out.println("Trophées sélectionnés pour cette partie :");
        System.out.println(this.trophees);
    }
    /**
     * Configure la variante de calcul des scores.
     */
    public void configurerVariante() {
        Scanner s = new Scanner(System.in);
        System.out.println("Veuillez choisir une variante de calcul des scores :");
        System.out.println("1 - Variante Normale");
        System.out.println("2 - Variante Joker");
        System.out.println("3 - Variante Couleur");
        int choixVariante = s.nextInt();
        s.nextLine(); // je "mange" le retour à la ligne
        while (choixVariante < 1 || choixVariante > 3) {
            System.out.println("Choix invalide. Veuillez choisir une variante de calcul des scores :");
            System.out.println("1 - Variante Normale");
            System.out.println("2 - Variante Joker");
            System.out.println("3 - Variante Couleur");
            choixVariante = s.nextInt();
            s.nextLine(); // je "mange" le retour à la ligne
        }
        switch (choixVariante) {
            case 1 -> this.variante = new VarianteNormale();
            case 2 -> this.variante = new VarianteJoker();
            case 3 -> this.variante = new VarianteCouleur();
        }
    }
    /**
     * Configure les joueurs de la partie.
     * <p>
     * Permet de choisir le nombre de joueurs,
     * leur nom et leur type (humain ou ordinateur).
     */
    public void configurerJoueurs(){
        Scanner s = new Scanner(System.in);
        System.out.println("Veuillez choisir un nombre de joueurs :");
        int nbJoueurs = s.nextInt();
        s.nextLine();
        while(nbJoueurs < 2 || nbJoueurs > 4) {
            System.out.println("Nombre de joueurs invalide. Veuillez choisir un nombre de joueurs (entre 2 et 4) :");
            nbJoueurs = s.nextInt();
            s.nextLine();
        }

        for(int i = 0; i < nbJoueurs; i++) {

            System.out.println("Veuillez entrer le nom du joueur " + (i + 1) + " :");
            String nomJoueur = s.nextLine();

            boolean saisieValide = false;

            while (!saisieValide) {
                System.out.println("Le joueur " + (i + 1) + " (" + nomJoueur + ") est-il un humain ou un ordinateur ? (H/O)");
                String input = s.nextLine().trim().toUpperCase();

                if (input.equals("H")) {
                    this.joueurs.add(new Humain(nomJoueur));
                    saisieValide = true;

                } else if (input.equals("O")) {
                    Ordinateur ordinateur = new Ordinateur(nomJoueur);

                    System.out.println("Choisissez la stratégie de l'ordinateur " + (i + 1) + " (" + nomJoueur + ") :");
                    System.out.println("1 - Stratégie Aléatoire");
                    System.out.println("2 - Stratégie Intelligente");
                    int strategieChoisie = s.nextInt();
                    s.nextLine();

                    if (strategieChoisie == 1) {
                        ordinateur.setStrategie(new Strategie1());
                    } else if (strategieChoisie == 2) {
                        ordinateur.setStrategie(new Strategie2());
                    }
                    this.joueurs.add(ordinateur);

                    saisieValide = true;

                } else {
                    System.out.println("Saisie incorrecte ! Veuillez entrer H pour humain ou O pour ordinateur.");
                }
            }
        }
    }
    /**
     * Configure le paquet de cartes utilisé pour la partie.
     * <p>
     * Le paquet de base est créé, puis l'utilisateur peut
     * ajouter des cartes spéciales.
     */
    public void configurerCartes(){
        cartes = creerPaquetBase();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Voulez-vous ajouter des cartes spéciales à la partie ? (o/N)");
        String reponse = scanner.nextLine().trim().toUpperCase();

        while (reponse.equals("O")){
            cartes.ajouterCarte(creerCarteExtension());

            System.out.println("Voulez-vous ajouter une carte spéciale supplémentaire? (o/N)");
            reponse = scanner.nextLine().trim().toUpperCase();
            while (reponse.equals("O")){
                cartes.ajouterCarte(creerCarteExtension());

                System.out.println("Voulez-vous ajouter une carte spéciale supplémentaire? (o/N)");
                reponse = scanner.nextLine().trim().toUpperCase();
            }
        }
    }
    /**
     * Crée une carte "extension" à partir des choix de l'utilisateur.
     * <p>
     * L'utilisateur peut créer :
     * - une carte normale (caractère + couleur + code trophée)
     * - un Joker (sans caractère ni couleur) avec un code trophée
     *
     * @return la carte créée
     */
    public Carte creerCarteExtension(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choisissez le chiffre de la carte à créer (2, 3, 4, As ou appuyez seulement sur entrée pour un joker) :");
        String reponseCaractere = scanner.nextLine().trim().toUpperCase();

        Carte.Caractere caractere = null;

        switch (reponseCaractere) {
            case "2" -> caractere = Carte.Caractere.Deux;
            case "3" -> caractere = Carte.Caractere.Trois;
            case "4" -> caractere = Carte.Caractere.Quatre;
            case "AS" -> caractere = Carte.Caractere.As;
            default -> {
                System.out.println("Sélectionnez un code trophée pour le joker (ex : BJ) :");
                String reponseTrophee = scanner.nextLine().trim().toUpperCase();
                while (!java.util.Arrays.asList(codesTropheesExistants).contains(reponseTrophee)) {
                    System.out.println("Choix invalide. Choisissez la couleur de la carte à créer (Carreau (ca), Coeur (co), Trefle (t), Pique (p)) :");
                    reponseTrophee = scanner.nextLine().trim().toUpperCase();
                }
                return new Carte(caractere, null, true, reponseTrophee);
            }
        }

        System.out.println("Choisissez la couleur de la carte à créer (Carreau (ca), Coeur (co), Trefle (t), Pique (p)) :");
        String reponseCouleur = scanner.nextLine().trim().toUpperCase();
        while(!reponseCouleur.equals("CA") && !reponseCouleur.equals("CO") && !reponseCouleur.equals("T") && !reponseCouleur.equals("P")){
            System.out.println("Choix invalide. Choisissez la couleur de la carte à créer (Carreau (ca), Coeur (co), Trefle (t), Pique (p)) :");
            reponseCouleur = scanner.nextLine().trim().toUpperCase();
        }

        Carte.Couleurs couleur = switch (reponseCouleur) {
            case "CA" -> Carte.Couleurs.Carreau;
            case "CO" -> Carte.Couleurs.Coeur;
            case "T" -> Carte.Couleurs.Trefle;
            default -> Carte.Couleurs.Pique;
        };

        String[] codesTropheesExistants = {"M2","M3","M4", "MA", "J", "HT", "HP", "HCA", "HCO", "NB2", "NB3", "NB4", "NBA", "LCA", "LCO", "LT", "LP", "BJNJ", "BJ"};
        System.out.println("Choisissez le code trophée de la nouvelle carte :");
        String reponseTrophee = scanner.nextLine().trim().toUpperCase();
        while(!java.util.Arrays.asList(codesTropheesExistants).contains(reponseTrophee)){
            System.out.println("Choix invalide. Choisissez le code trophée de la carte à créer :");
            reponseTrophee = scanner.nextLine().trim().toUpperCase();
        }

        return new Carte(caractere,couleur,false,reponseTrophee);
    }
    /**
     * Lance et gère le déroulement complet de la partie.
     * <p>
     * La partie se déroule par manches successives jusqu'à
     * ce que la condition de fin soit atteinte.
     */
    public void jouerPartie() {

        Scanner sc = new Scanner(System.in);

        while (!estTerminee()) {

            recupererCartesDesOffres();

            constituerPaquetManche();

            distribuerCartes();

            faireOffres();

            selectionnerOffres();

            System.out.print("Voulez-vous sauvegarder la partie ? (o/N) : ");
            String rep = sc.nextLine().trim().toUpperCase();

            if (rep.equals("O")) {
                System.out.print("Nom du fichier de sauvegarde : ");
                String fichier = sc.nextLine();
                sauvegarderPartie(fichier);
                System.out.println("modele.Partie sauvegardée !");
            }
        }

        distribuerTrophees();
        accept(variante);
        afficherFinJeu();
    }

    /**
     * Distribue les cartes aux joueurs pour une manche.
     */
    public void distribuerCartes() {
        for (Joueur joueur : this.joueurs) {
            this.paquetManche.distribuer(joueur, 2);
        }
    }
    /**
     * Sauvegarde l'état courant de la partie dans un fichier.
     *
     * @param fichier le nom du fichier de sauvegarde
     */
    public void sauvegarderPartie(String fichier) {

        try (PrintWriter out = new PrintWriter(new FileWriter(fichier))) {

            for (Joueur j : joueurs) {

                out.println("JOUEUR");
                out.println(j instanceof Humain ? "HUMAIN" : "ORDINATEUR");
                out.println(j.getNom());
                out.println(j.getScore());

                out.println("MAIN");
                for (Carte c : j.getMain().getCartes()) {
                    ecrireCarte(out, c);
                }

                out.println("JEST");
                for (Carte c : j.getJest().getCartes()) {
                    ecrireCarte(out, c);
                }

                out.println("OFFRE");
                if (j.getOffre() != null) {
                    ecrireCarte(out, j.getOffre().getCarteFaceAvant());
                    ecrireCarte(out, j.getOffre().getCarteFaceCachee());
                } else {
                    out.println("null");
                    out.println("null");
                }
            }

            out.println("PIOCHE");
            for (Carte c : cartes.getCartes()) {
                ecrireCarte(out, c);
            }

            out.println("PAQUET_MANCHE");
            for (Carte c : paquetManche.getCartes()) {
                ecrireCarte(out, c);
            }

        } catch (IOException e) {
            System.out.println("Erreur sauvegarde : " + e.getMessage());
        }
    }

    /**
     * Charge une partie précédemment sauvegardée depuis un fichier.
     *
     * @param fichier le nom du fichier de sauvegarde
     */
    public void chargerPartie(String fichier) {

        try (Scanner sc = new Scanner(new File(fichier))) {

            joueurs.clear();
            cartes.getCartes().clear();
            paquetManche.getCartes().clear();

            while (sc.hasNextLine()) {

                String ligne = sc.nextLine();

                switch (ligne) {
                    case "JOUEUR" -> {

                        String type = sc.nextLine();
                        String nom = sc.nextLine();
                        int score = Integer.parseInt(sc.nextLine());

                        Joueur j;
                        if (type.equals("HUMAIN")) {
                            j = new Humain(nom);
                        } else {
                            Ordinateur o = new Ordinateur(nom);
                            o.setStrategie(new Strategie1()); // IMPORTANT
                            j = o;
                        }
                        j.setScore(score);

                        sc.nextLine();
                        ligne = sc.nextLine();
                        while (!ligne.equals("JEST")) {
                            j.getMain().ajouterCarte(lireCarte(ligne, sc));
                            ligne = sc.nextLine();
                        }

                        ligne = sc.nextLine();
                        while (!ligne.equals("OFFRE")) {
                            j.getJest().ajouterCarte(lireCarte(ligne, sc));
                            ligne = sc.nextLine();
                        }

                        Carte v = lireCarte(sc.nextLine(), sc);
                        Carte c = lireCarte(sc.nextLine(), sc);
                        if (v != null || c != null) {
                            j.setOffre(new Offre(v, c));
                        }

                        joueurs.add(j);
                    }
                    case "PIOCHE" -> {
                        ligne = sc.nextLine();
                        while (!ligne.equals("PAQUET_MANCHE")) {
                            cartes.ajouterCarte(lireCarte(ligne, sc));
                            ligne = sc.nextLine();
                        }
                    }
                    case "PAQUET_MANCHE" -> {
                        while (sc.hasNextLine()) {
                            paquetManche.ajouterCarte(lireCarte(sc.nextLine(), sc));
                        }
                    }
                }
            }

            if (this.variante == null) {
                this.variante = new VarianteNormale();
            }

        } catch (Exception e) {
            System.out.println("Erreur chargement : " + e.getMessage());
        }
    }

    /**
     * Écrit une carte dans un fichier de sauvegarde.
     * <p>
     * Le format écrit correspond à celui attendu par la méthode lireCarte.
     * Si la carte est null, écrit "null" sur une seule ligne.
     *
     * @param out le flux d'écriture
     * @param c la carte à écrire (peut être null)
     */
    private void ecrireCarte(PrintWriter out, Carte c) {
        if (c == null) {
            out.println("null");
        } else {
            out.println(c.isEstJoker());
            out.println(c.getCaractere());
            out.println(c.getCouleur());
            out.println(c.getCodeTrophee());
        }
    }
    /**
     * Lit une carte depuis un fichier de sauvegarde.
     * <p>
     * Le paramètre premiereLigne correspond à la première ligne déjà lue :
     * - "null" signifie que la carte est absente
     * - sinon, cette ligne contient le booléen estJoker
     * <p>
     * Les lignes suivantes sont lues dans l'ordre :
     * - caractère (ou "null")
     * - couleur (ou "null")
     * - code trophée (ou "null")
     *
     * @param premiereLigne première ligne décrivant la carte (ou "null")
     * @param sc scanner utilisé pour lire la suite
     * @return la carte reconstruite ou null si premiereLigne vaut "null"
     */
    private Carte lireCarte(String premiereLigne, Scanner sc) {

        if (premiereLigne.equals("null")) {
            return null;
        }

        boolean estJoker = Boolean.parseBoolean(premiereLigne);

        String carLine = sc.nextLine();
        Carte.Caractere caractere =
                carLine.equals("null") ? null : Carte.Caractere.valueOf(carLine);

        String coulLine = sc.nextLine();
        Carte.Couleurs couleur =
                coulLine.equals("null") ? null : Carte.Couleurs.valueOf(coulLine);

        String code = sc.nextLine();
        if (code.equals("null")) code = null;

        return new Carte(caractere, couleur, estJoker, code);
    }

    /**
     * Calcule les scores finaux à l'aide de la variante choisie.
     *
     * @param visitor la variante de calcul des scores
     */
    public void accept(VarianteVisitor visitor) {

        for (Joueur joueur : joueurs) {
            int score = visitor.visit(joueur.getJest());
            joueur.setScore(score);
        }
    }

    /**
     * Indique si la partie est terminée.
     *
     * @return true si la partie est terminée, false sinon
     */
    public boolean estTerminee(){
        return this.cartes.getCartes().size() < this.joueurs.size();
    }
    /**
     * Attribue les trophées aux joueurs selon les règles du jeu.
     */
    public void distribuerTrophees() {

        for (Carte trophee : this.trophees) {

            String code = trophee.getCodeTrophee();
            Joueur gagnant = null;

            switch (code) {

                case "M2", "M3", "M4", "MA" -> {
                    Carte.Caractere car = switch (code) {
                        case "M2" -> Carte.Caractere.Deux;
                        case "M3" -> Carte.Caractere.Trois;
                        case "M4" -> Carte.Caractere.Quatre;
                        default -> Carte.Caractere.As;
                    };

                    int max = -1;
                    for (Joueur j : this.joueurs) {
                        int nb = j.getJest().compterCaractere(car);
                        if (nb > max) {
                            max = nb;
                            gagnant = j;
                        }
                    }
                }

                case "J" -> {
                    for (Joueur j : this.joueurs) {
                        if (j.getJest().contientJoker()) {
                            gagnant = j;
                            break;
                        }
                    }
                }

                case "HP", "HT", "HCA", "HCO", "LP", "LT", "LCA", "LCO" -> {

                    boolean highest = code.startsWith("H");

                    Carte.Couleurs couleur = switch (code.substring(1)) {
                        case "P" -> Carte.Couleurs.Pique;
                        case "T" -> Carte.Couleurs.Trefle;
                        case "CA" -> Carte.Couleurs.Carreau;
                        default -> Carte.Couleurs.Coeur;
                    };

                    int bestValue = highest ? -1 : Integer.MAX_VALUE;

                    for (Joueur j : this.joueurs) {
                        Carte c = j.getJest().getCarteExtremeCouleur(couleur, highest);
                        if (c != null) {
                            int v = c.getValeurCarte();
                            if ((highest && v > bestValue) || (!highest && v < bestValue)) {
                                bestValue = v;
                                gagnant = j;
                            }
                        }
                    }
                }

                case "BJ", "BJNJ" -> {

                    int best = -1;

                    for (Joueur j : this.joueurs) {
                        if (code.equals("BJNJ") && j.getJest().contientJoker()) continue;

                        int total = 0;
                        for (Carte c : j.getJest().getCartes()) {
                            total += c.getValeurCarte();
                        }

                        if (total > best) {
                            best = total;
                            gagnant = j;
                        }
                    }
                }
            }

            if (gagnant != null) {
                gagnant.getJest().ajouterCarte(trophee);
            }
        }
        for (Joueur j : this.joueurs) {
            System.out.println(j.getNom() + " jest : " + j.getJest());
        }
    }
    /**
     * Affiche les scores finaux et le gagnant de la partie.
     */
    public void afficherFinJeu(){
        System.out.println("La partie est terminée !");
        System.out.println("Scores finaux :");
        Joueur gagnant = joueurs.getFirst();
        for (Joueur joueur : this.joueurs) {
            if (joueur.getScore() > gagnant.getScore()) {
                gagnant = joueur;
            }
            System.out.println(joueur.getNom() + " : " + joueur.getScore() + " points");
        }
        System.out.println("Le gagnant est : " + gagnant.getNom() + " avec " + gagnant.getScore() + " points !");
    }
    /**
     * Détermine le premier joueur à jouer lors de la sélection des offres.
     * <p>
     * Le premier joueur est celui qui possède la meilleure carte face visible
     * (valeur la plus élevée, puis départage par couleur via l'ordre des enums).
     *
     * @return le joueur qui commence la phase de sélection des offres
     */
    public Joueur determinerPremierJoueur(){
        Joueur premierJoueur = this.joueurs.getFirst();
        Carte meilleureCarte = premierJoueur.getOffre().getCarteFaceAvant();

        Carte.Couleurs couleur = null;

        for (Joueur joueur : this.joueurs) {
            Carte faceVisible = joueur.getOffre().getCarteFaceAvant();
            if (faceVisible.getValeurCarte() > meilleureCarte.getValeurCarte()) {
                meilleureCarte = faceVisible;
                premierJoueur = joueur;
            }
            else if (faceVisible.getValeurCarte() == meilleureCarte.getValeurCarte()) {
                if (faceVisible.getCouleur().ordinal() > meilleureCarte.getCouleur().ordinal()) {
                    meilleureCarte = faceVisible;
                    premierJoueur = joueur;
                }
            }
        }

        return premierJoueur;
    }
    /**
     * Constitue le paquet de manche à partir de la pioche principale.
     * <p>
     * Si le paquet de manche est vide, on y place deux cartes par joueur.
     * Sinon, on mélange le paquet de manche et on y ajoute une carte par joueur.
     */
    public void constituerPaquetManche(){
        if (this.paquetManche.getCartes().isEmpty()){
            this.cartes.melanger();
            for (Joueur joueur : this.joueurs) {
                // a refaire au propre
                this.paquetManche.ajouterCarte(this.cartes.getCarteDessus());
                this.paquetManche.ajouterCarte(this.cartes.getCarteDessus());
            }
        } else {
            this.paquetManche.melanger();
            for (int i = 0; i < this.joueurs.size(); i++) {
                this.paquetManche.ajouterCarte(this.cartes.getCarteDessus());
            }
        }
    }

    /**
     * Gère la sélection des offres par les joueurs.
     * <p>
     * Les joueurs prennent les offres à tour de rôle
     * jusqu'à ce qu'il n'y ait plus d'offres disponibles.
     */
    public void selectionnerOffres() {

        Scanner s = new Scanner(System.in);

        ArrayList<Joueur> joueursAyantPasJoue = new ArrayList<>(joueurs);

        Joueur joueurSuivant = determinerPremierJoueur();

        if (joueurSuivant == null) {
            throw new IllegalStateException("Impossible de déterminer le premier joueur");
        }

        while (encoreDesOffresDispo()) {

            if (!joueursAyantPasJoue.contains(joueurSuivant)) {
                joueurSuivant = joueursAyantPasJoue.getFirst();
            }

            ArrayList<Joueur> joueursDispo = new ArrayList<>();

            for (Joueur j : joueurs) {
                if ((j != joueurSuivant && j.getOffre() != null && j.getOffre().getStatutOffre()) || (j == joueurSuivant
                        && joueursAyantPasJoue.size() == 1)) {
                    joueursDispo.add(j);
                }
            }

            if (joueursDispo.isEmpty()) {
                break;
            }

            if (joueurSuivant instanceof Ordinateur) {

                joueurSuivant.prendreUneOffre(null, false, joueursDispo);

                joueursAyantPasJoue.remove(joueurSuivant);

                for (Joueur j : joueursDispo) {
                    if (!j.getOffre().getStatutOffre()) {
                        joueurSuivant = j;
                        break;
                    }
                }
            }

            else {

                System.out.println("Offres disponibles :");
                for (int i = 0; i < joueursDispo.size(); i++) {
                    Joueur j = joueursDispo.get(i);
                    System.out.println(i + " - " + j.getNom() + " : " + j.getOffre());
                }

                System.out.println(joueurSuivant.getNom()
                        + ", choisissez une offre à prendre (entrez le numéro correspondant) :");

                int choix = s.nextInt();
                s.nextLine();

                while (choix < 0 || choix >= joueursDispo.size()) {
                    System.out.println("Sélection invalide. Veuillez réessayer :");
                    choix = s.nextInt();
                    s.nextLine();
                }

                Joueur cible = joueursDispo.get(choix);

                System.out.println("Voulez-vous prendre la carte face visible (V) ou face cachée (C) ?");
                String choixCarte = s.nextLine().trim().toUpperCase();

                while (!choixCarte.equals("V") && !choixCarte.equals("C")) {
                    System.out.println("Choix invalide. Entrez V ou C :");
                    choixCarte = s.nextLine().trim().toUpperCase();
                }

                joueurSuivant.prendreUneOffre(cible, choixCarte.equals("C"), joueurs);
                joueursAyantPasJoue.remove(joueurSuivant);
                joueurSuivant = cible;
            }
        }
    }


    /**
     * Permet aux joueurs de créer leurs offres.
     */
    public void faireOffres(){
        for (Joueur joueur : this.joueurs) {
            joueur.setOffre(joueur.faireUneOffre());
        }
    }
    /**
     * Indique s'il reste au moins une offre disponible.
     * <p>
     * Une offre est considérée disponible si son statut est à true.
     *
     * @return true s'il reste des offres disponibles, false sinon
     */
    public boolean encoreDesOffresDispo(){
        for (Joueur joueur : this.joueurs) {
            if (joueur.getOffre().getStatutOffre()) {
                return true;
            }
        }
        return false;
    }
    /**
     * Récupère les cartes restantes dans les offres des joueurs
     * et les remet dans le paquet de manche.
     * <p>
     * Cette méthode est utile en début de manche pour éviter de perdre
     * des cartes non prises lors de la manche précédente.
     * Les cartes récupérées sont retirées des offres et l'offre est fermée.
     */
    public void recupererCartesDesOffres() {

        for (Joueur joueur : joueurs) {

            Offre offre = joueur.getOffre();
            if (offre == null) continue;

            if (offre.getCarteFaceAvant() != null) {
                paquetManche.ajouterCarte(offre.getCarteFaceAvant());
                offre.setCarteFaceAvant(null);
            }

            if (offre.getCarteFaceCachee() != null) {
                paquetManche.ajouterCarte(offre.getCarteFaceCachee());
                offre.setCarteFaceCachee(null);
            }

            offre.setStatutOffre(false);
        }
    }

    public String[] getCodesTropheesExistants() {
        return codesTropheesExistants;
    }

    public VarianteVisitor getVariante() {
        return variante;
    }
}
