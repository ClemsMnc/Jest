import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
public class Partie {
    ArrayList<Carte> trophees = new ArrayList<>();
    ArrayList<Joueur> joueurs = new ArrayList<>();
    Paquet cartes = new Paquet();
    Paquet paquetManche = new Paquet();
    VarianteVisitor variante = null;
    String[] codesTropheesExistants = {"M2","M3","M4", "MA", "J", "HT", "HP", "HCA", "HCO", "NB2", "NB3", "NB4", "NBA", "LCA", "LCO", "LT", "LP", "BJNJ", "BJ"};


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
            System.out.println("Partie chargée avec succès !");
        } else {
            partie.configurerPartie();
        }

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

    public void configurerPartie() {
        configurerJoueurs();
        configurerCartes();
        configurerVariante();
        configurerTrophees();
    }

    public void configurerTrophees() {
        this.cartes.melanger();
        this.trophees.add(this.cartes.getCarteDessus());
        this.trophees.add(this.cartes.getCarteDessus());
        System.out.println("Trophées sélectionnés pour cette partie :");
        System.out.println(this.trophees);
    }

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

    public void configurerJoueurs(){
        Scanner s = new Scanner(System.in);
        System.out.println("Veuillez choisir un nombre de joueurs :");
        int nbJoueurs = s.nextInt();
        s.nextLine(); // je "mange" le retour à la ligne
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

    public Carte creerCarteExtension(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Choisissez le chiffre de la carte à créer (2, 3, 4, As ou appuyez seulement sur entrée pour un joker) :");
        String reponseCaractere = scanner.nextLine().trim().toUpperCase();

        Carte.Caractere caractere = null;

        if (reponseCaractere.equals("2")) {
            caractere = Carte.Caractere.Deux;
        } else if (reponseCaractere.equals("3")) {
            caractere = Carte.Caractere.Trois;
        } else if (reponseCaractere.equals("4")) {
            caractere = Carte.Caractere.Quatre;
        } else if (reponseCaractere.equals("AS")) {
            caractere = Carte.Caractere.As;
        } else {
            System.out.println("Sélectionnez un code trophée pour le joker (ex : BJ) :");
            String reponseTrophee = scanner.nextLine().trim().toUpperCase();
            while(!java.util.Arrays.asList(codesTropheesExistants).contains(reponseTrophee)) {
                System.out.println("Choix invalide. Choisissez la couleur de la carte à créer (Carreau (ca), Coeur (co), Trefle (t), Pique (p)) :");
                reponseTrophee = scanner.nextLine().trim().toUpperCase();
            }
            return new Carte(caractere, null, true, reponseTrophee);
        }

        System.out.println("Choisissez la couleur de la carte à créer (Carreau (ca), Coeur (co), Trefle (t), Pique (p)) :");
        String reponseCouleur = scanner.nextLine().trim().toUpperCase();
        while(!reponseCouleur.equals("CA") && !reponseCouleur.equals("CO") && !reponseCouleur.equals("T") && !reponseCouleur.equals("P")){
            System.out.println("Choix invalide. Choisissez la couleur de la carte à créer (Carreau (ca), Coeur (co), Trefle (t), Pique (p)) :");
            reponseCouleur = scanner.nextLine().trim().toUpperCase();
        }

        Carte.Couleurs couleur = null;

        if (reponseCouleur.equals("CA")) {
            couleur = Carte.Couleurs.Carreau;
        } else if (reponseCouleur.equals("CO")) {
            couleur = Carte.Couleurs.Coeur;
        } else if (reponseCouleur.equals("T")) {
            couleur = Carte.Couleurs.Trefle;
        } else {
            couleur = Carte.Couleurs.Pique;
        }

        String[] codesTropheesExistants = {"M2","M3","M4", "MA", "J", "HT", "HP", "HCA", "HCO", "NB2", "NB3", "NB4", "NBA", "LCA", "LCO", "LT", "LP", "BJNJ", "BJ"};
        System.out.println("Choisissez le code trophée de la nouvelle carte :");
        String reponseTrophee = scanner.nextLine().trim().toUpperCase();
        while(!java.util.Arrays.asList(codesTropheesExistants).contains(reponseTrophee)){
            System.out.println("Choix invalide. Choisissez le code trophée de la carte à créer :");
            reponseTrophee = scanner.nextLine().trim().toUpperCase();
        }

        return new Carte(caractere,couleur,false,reponseTrophee);
    }

    public void jouerPartie() {

        Scanner sc = new Scanner(System.in);

        while (!estTerminee()) {

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
                System.out.println("Partie sauvegardée !");
            }
        }

        distribuerTrophees();
        accept(variante);
        afficherFinJeu();
    }


    public void distribuerCartes() {
        for (Joueur joueur : this.joueurs) {
            this.paquetManche.distribuer(joueur, 2);
        }
    }

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


    public void chargerPartie(String fichier) {

        try (Scanner sc = new Scanner(new File(fichier))) {

            joueurs.clear();
            cartes.getCartes().clear();
            paquetManche.getCartes().clear();

            String ligne;

            while (sc.hasNextLine()) {

                ligne = sc.nextLine();

                if (ligne.equals("JOUEUR")) {

                    String type = sc.nextLine(); // HUMAIN / ORDINATEUR
                    String nom = sc.nextLine();
                    int score = Integer.parseInt(sc.nextLine());

                    Joueur j;
                    if (type.equals("HUMAIN")) {
                        j = new Humain(nom);
                    } else {
                        j = new Ordinateur(nom);
                    }
                    j.setScore(score);

                    sc.nextLine(); // MAIN
                    ligne = sc.nextLine();
                    while (!ligne.equals("JEST")) {
                        j.getMain().ajouterCarte(lireCarte(ligne, sc));
                        ligne = sc.nextLine();
                    }

                    ligne = sc.nextLine(); // première carte JEST
                    while (!ligne.equals("OFFRE")) {
                        j.getJest().ajouterCarte(lireCarte(ligne, sc));
                        ligne = sc.nextLine();
                    }

                    Carte visible = lireCarte(sc.nextLine(), sc);
                    Carte cachee  = lireCarte(sc.nextLine(), sc);
                    if (visible != null || cachee != null) {
                        j.setOffre(new Offre(visible, cachee));
                    }

                    joueurs.add(j);
                }

                else if (ligne.equals("PIOCHE")) {
                    ligne = sc.nextLine();
                    while (!ligne.equals("PAQUET_MANCHE")) {
                        cartes.ajouterCarte(lireCarte(ligne, sc));
                        ligne = sc.nextLine();
                    }
                }

                else if (ligne.equals("PAQUET_MANCHE")) {
                    while (sc.hasNextLine()) {
                        paquetManche.ajouterCarte(lireCarte(sc.nextLine(), sc));
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Erreur chargement : " + e.getMessage());
        }
    }


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

    private Carte lireCarte(String premiereLigne, Scanner sc) {
        if (premiereLigne.equals("null")) {
            return null;
        }

        boolean estJoker = Boolean.parseBoolean(premiereLigne);
        Carte.Caractere caractere = Carte.Caractere.valueOf(sc.nextLine());
        Carte.Couleurs couleur = Carte.Couleurs.valueOf(sc.nextLine());
        String codeTrophee = sc.nextLine();

        return new Carte(caractere, couleur, estJoker, codeTrophee);
    }

    public void accept(VarianteVisitor visitor) {

        // Pour chaque joueur, on calcule le score à partir de son Jest
        for (Joueur joueur : joueurs) {
            int score = visitor.visit(joueur.getJest());
            joueur.setScore(score);
        }
    }


    public boolean estTerminee(){
        return this.cartes.getCartes().size() < this.joueurs.size();
    }

    public void distribuerTrophees() {

        for (Carte trophee : this.trophees) {

            String code = trophee.getCodeTrophee();
            Joueur gagnant = null;

            switch (code) {

                /* ================= MAJORITY ================= */
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

                /* ================= JOKER ================= */
                case "J" -> {
                    for (Joueur j : this.joueurs) {
                        if (j.getJest().contientJoker()) {
                            gagnant = j;
                            break;
                        }
                    }
                }

                /* ================= HIGHEST / LOWEST COULEUR ================= */
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

                /* ================= BEST JEST ================= */
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
                gagnant.getJest().getCartes().add(trophee);
            }
        }
    }


    public void afficherFinJeu(){
        System.out.println("La partie est terminée !");
        System.out.println("Scores finaux :");
        for (Joueur joueur : this.joueurs) {
            System.out.println(joueur.getNom() + " : " + joueur.getScore() + " points");
        }
        System.out.println("Le gagnant est :");
    }

    public Joueur determinerPremierJoueur(){
        Carte meilleureCarte = this.joueurs.getFirst().offre.getCarteFaceAvant();
        Joueur premierJoueur = null;
        Carte.Couleurs couleur = null;

        for (Joueur joueur : this.joueurs) {
            Carte faceVisible = joueur.offre.getCarteFaceAvant();
            if (faceVisible.getValeurCarte() > meilleureCarte.getValeurCarte()) {
                meilleureCarte = faceVisible;
                premierJoueur = joueur;
            }
            // Gestion des égalités de valeur, on classe grâce à la couleur
            else if (faceVisible.getValeurCarte() == meilleureCarte.getValeurCarte()) {
                if (faceVisible.getCouleur().ordinal() > meilleureCarte.getCouleur().ordinal()) {
                    meilleureCarte = faceVisible;
                    premierJoueur = joueur;
                }
            }
        }

        return premierJoueur;
    }

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

    public void selectionnerOffres() {
        Scanner s = new Scanner(System.in);
        Joueur joueurSuivant = determinerPremierJoueur();
        ArrayList<Joueur> joueursRestants = new ArrayList<>(this.joueurs);
        joueursRestants.remove(joueurSuivant);

        while (encoreDesOffresDispo()) {
            if (joueurSuivant instanceof Ordinateur) {
                joueurSuivant.prendreUneOffre(null,false,joueursRestants);
                joueursRestants = new ArrayList<>(this.joueurs);
                joueursRestants.remove(joueurSuivant);
            } else {
                afficherOffresDisponibles(joueurSuivant);
                System.out.println(joueurSuivant.getNom() + ", choisissez une offre à prendre (entrez le numéro correspondant) :");
                int offreSelectionnee = s.nextInt();
                s.nextLine();
                while (offreSelectionnee < 0 || offreSelectionnee >= joueursRestants.size()) {
                    System.out.println("Sélection invalide. Veuillez sélectionner une offre disponible.");
                    offreSelectionnee = s.nextInt();
                    s.nextLine();
                }
                Joueur cible = joueursRestants.get(offreSelectionnee);
                System.out.println("Voulez-vous prendre la carte face visible (V) ou face cachée (C) ?");
                String choixCarte = s.next().trim().toUpperCase();
                while (!(choixCarte.equals("V") || choixCarte.equals("C"))) {
                    System.out.println("Choix invalide. Voulez-vous prendre la carte face visible (V) ou face cachée (C) ?");
                    choixCarte = s.next().trim().toUpperCase();
                }

                joueurSuivant.prendreUneOffre(cible, choixCarte.equals("C"), this.joueurs);
                joueurSuivant = cible;
            }
        }
    }

    public void afficherOffresDisponibles(Joueur joueurSuivant) {
        System.out.println("Offres disponibles :");
        int i = 0;
        for (Joueur joueur : this.joueurs) {
            if (joueur.offre.getStatutOffre()) {
                if (joueur == joueurSuivant) {
                    System.out.println(i + " - " + joueur.getNom() + " : Offre non disponible (vous ne pouvez pas prendre votre propre offre)");
                } else {
                    System.out.println(i + " - " + joueur.getNom() + " : " + joueur.offre);
                }
                i++;
            }
        }
    }

    public void faireOffres(){
        for (Joueur joueur : this.joueurs) {
            joueur.setOffre(joueur.faireUneOffre());
        }
    }

    public boolean encoreDesOffresDispo(){
        for (Joueur joueur : this.joueurs) {
            if (joueur.offre.getStatutOffre()) {
                return true;
            }
        }
        return false;
    }
}
