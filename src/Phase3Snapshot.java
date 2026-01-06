import java.util.List;

public class Phase3Snapshot {

    public final String phase;
    public final String message;

    // setup
    public final int nbJoueursAttendu;
    public final int nbJoueursActuel;
    public final String variante;
    public final List<String> joueursConfig;

    // game
    public final int manche;
    public final String joueurCourant;
    public final List<String> mainHumaine;          // si on attend offer
    public final List<String> ciblesDisponibles;    // si on attend take

    public final List<String> offresAffichage;
    public final List<String> scoresAffichage;

    public final boolean partieFinie;

    public Phase3Snapshot(
            String phase,
            String message,
            int nbJoueursAttendu,
            int nbJoueursActuel,
            String variante,
            List<String> joueursConfig,
            int manche,
            String joueurCourant,
            List<String> mainHumaine,
            List<String> ciblesDisponibles,
            List<String> offresAffichage,
            List<String> scoresAffichage,
            boolean partieFinie
    ) {
        this.phase = phase;
        this.message = message;
        this.nbJoueursAttendu = nbJoueursAttendu;
        this.nbJoueursActuel = nbJoueursActuel;
        this.variante = variante;
        this.joueursConfig = joueursConfig;
        this.manche = manche;
        this.joueurCourant = joueurCourant;
        this.mainHumaine = mainHumaine;
        this.ciblesDisponibles = ciblesDisponibles;
        this.offresAffichage = offresAffichage;
        this.scoresAffichage = scoresAffichage;
        this.partieFinie = partieFinie;
    }

    public String toCliString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== PHASE3 | ").append(phase).append(" ===\n");
        if (message != null && !message.isBlank()) sb.append(message).append("\n");

        if (phase.startsWith("SETUP")) {
            sb.append("\nCONFIG:\n");
            sb.append("- nb joueurs attendu: ").append(nbJoueursAttendu).append("\n");
            sb.append("- nb joueurs actuel : ").append(nbJoueursActuel).append("\n");
            sb.append("- variante          : ").append(variante).append("\n");
            sb.append("- joueurs:\n");
            for (String j : joueursConfig) sb.append("  * ").append(j).append("\n");

            sb.append("\nCommandes CLI config:\n");
            sb.append("players <n>\n");
            sb.append("human <Nom>\n");
            sb.append("ai <Nom> s1|s2\n");
            sb.append("variant normale|joker|couleur\n");
            sb.append("start\n");
            return sb.toString();
        }

        sb.append("\nMANCHE: ").append(manche).append("\n");
        sb.append("Joueur courant: ").append(joueurCourant == null ? "-" : joueurCourant).append("\n");

        sb.append("\nOFFRES:\n");
        for (String o : offresAffichage) sb.append(o).append("\n");

        sb.append("\nSCORES/JEST:\n");
        for (String s : scoresAffichage) sb.append(s).append("\n");

        if (mainHumaine != null && !mainHumaine.isEmpty()) {
            sb.append("\nMAIN (choisir carte cachée):\n");
            for (int i = 0; i < mainHumaine.size(); i++) sb.append(i).append(") ").append(mainHumaine.get(i)).append("\n");
            sb.append("Commande: offer 0|1\n");
        }

        if (ciblesDisponibles != null && !ciblesDisponibles.isEmpty()) {
            sb.append("\nCIBLES DISPONIBLES:\n");
            for (String c : ciblesDisponibles) sb.append("- ").append(c).append("\n");
            sb.append("Commande: take <Nom> visible|cachee\n");
        }

        sb.append("\nCommande générique: next\n");
        if (partieFinie) sb.append("\n*** FIN DE PARTIE ***\n");
        return sb.toString();
    }
}
