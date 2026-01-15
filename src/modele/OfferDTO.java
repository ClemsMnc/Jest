package modele;

public class OfferDTO {
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