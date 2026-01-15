package modele;

import java.util.List;

public class Phase3Snapshot {

    private final String phase;
    private final String message;

    // setup
    private final int nbJoueursAttendu;
    private final int nbJoueursActuel;
    private final String variante;
    private final List<String> joueursConfig;

    // game
    private final int manche;
    private final String joueurCourant;

    // si WAIT_OFFER_HUMAN : main à afficher (2 cartes)
    private final List<String> mainHumaine;

    // si WAIT_TAKE_HUMAN : cibles prenable (noms)
    private final List<String> ciblesDisponibles;

    // table
    private final List<OfferDTO> offres;

    // scores/jest (texte)
    private final List<String> scoresAffichage;

    private final boolean partieFinie;

    public String getPhase() {
        return phase;
    }

    public String getMessage() {
        return message;
    }

    public int getNbJoueursAttendu() {
        return nbJoueursAttendu;
    }

    public int getNbJoueursActuel() {
        return nbJoueursActuel;
    }

    public String getVariante() {
        return variante;
    }

    public List<String> getJoueursConfig() {
        return joueursConfig;
    }

    public int getManche() {
        return manche;
    }

    public String getJoueurCourant() {
        return joueurCourant;
    }

    public List<String> getMainHumaine() {
        return mainHumaine;
    }

    public List<String> getCiblesDisponibles() {
        return ciblesDisponibles;
    }

    public List<OfferDTO> getOffres() {
        return offres;
    }

    public List<String> getScoresAffichage() {
        return scoresAffichage;
    }

    public boolean isPartieFinie() {
        return partieFinie;
    }

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
}
