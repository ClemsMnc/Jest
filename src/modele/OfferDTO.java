package modele;

public class OfferDTO {

    private final String owner;
    private final String visibleText;
    private final boolean hasHiddenCard;
    private final boolean active;

    public OfferDTO(String owner, String visibleText, boolean hasHiddenCard, boolean active) {
        this.owner = owner;
        this.visibleText = visibleText;
        this.hasHiddenCard = hasHiddenCard;
        this.active = active;
    }

    public String getOwner() {
        return owner;
    }

    public String getVisibleText() {
        return visibleText;
    }

    public boolean hasHiddenCard() {
        return hasHiddenCard;
    }

    public boolean isActive() {
        return active;
    }
}