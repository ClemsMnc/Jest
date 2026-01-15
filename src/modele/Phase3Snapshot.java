package modele;

import java.util.List;

public class Phase3Snapshot {
    public static class OfferDTO {
        public final String owner;
        public final String visibleText;
        public final boolean hasHiddenCard;
        public final boolean active;

        public OfferDTO(String owner, String visibleText, boolean hasHiddenCard, boolean active) {
            this.owner = owner;
            this.visibleText = visibleText;
            this.hasHiddenCard = hasHiddenCard;
            this.active = active;
        }
    }

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

    // si WAIT_OFFER_HUMAN : main à afficher (2 cartes)
    public final List<String> mainHumaine;

    // si WAIT_TAKE_HUMAN : cibles prenable (noms)
    public final List<String> ciblesDisponibles;

    // table
    public final List<OfferDTO> offres;

    // scores/jest (texte)
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
            List<OfferDTO> offres,
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
        this.offres = offres;
        this.scoresAffichage = scoresAffichage;
        this.partieFinie = partieFinie;
    }

    public String toCliString() {
        StringBuilder sb = new StringBuilder();
        sb.append("=== JEST | ").append(phase).append(" ===\n");
        if (message != null && !message.isBlank()) sb.append(message).append("\n");

        if (phase.startsWith("SETUP")) {
            sb.append("\nCONFIG:\n");
            sb.append("- nb joueurs attendu: ").append(nbJoueursAttendu).append("\n");
            sb.append("- nb joueurs actuel : ").append(nbJoueursActuel).append("\n");
            sb.append("- variante          : ").append(variante).append("\n");
            sb.append("- joueurs:\n");
            for (String j : joueursConfig) sb.append("  * ").append(j).append("\n");

            sb.append("\nCommandes:\n");
            sb.append("players <n>\n");
            sb.append("human <Nom>\n");
            sb.append("ai <Nom> s1|s2\n");
            sb.append("variant normale|joker|couleur\n");
            sb.append("start\n");
            return sb.toString();
        }

        sb.append("\nMANCHE: ").append(manche).append("\n");
        sb.append("modele.Joueur courant: ").append(joueurCourant == null ? "-" : joueurCourant).append("\n");

        sb.append("\nOFFRES:\n");
        for (OfferDTO o : offres) {
            sb.append("- ").append(o.owner)
                    .append(" | V=").append(o.visibleText)
                    .append(" | C=").append(o.hasHiddenCard ? "(cachée)" : "-")
                    .append(" | active=").append(o.active)
                    .append("\n");
        }

        sb.append("\nSCORES/JEST:\n");
        for (String s : scoresAffichage) sb.append(s).append("\n");

        if (mainHumaine != null && !mainHumaine.isEmpty()) {
            sb.append("\nMAIN (cliquer/cmd offer 0|1):\n");
            for (int i = 0; i < mainHumaine.size(); i++) sb.append(i).append(") ").append(mainHumaine.get(i)).append("\n");
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
