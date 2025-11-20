public class Offre {
    private Carte carteFaceAvant;
    private Carte carteFaceCachee;
    private boolean statutOffre;

    public Offre(Carte carteFaceAvant, Carte carteFaceCachee) {
        this.carteFaceAvant = carteFaceAvant;
        this.carteFaceCachee = carteFaceCachee;
    }

    public Carte prendreCarte(boolean faceCachee){
        //TODO
        return this.carteFaceCachee;
    }


    public Carte getCarteFaceAvant() {
        return carteFaceAvant;
    }

    public void setCarteFaceAvant(Carte carteFaceAvant) {
        this.carteFaceAvant = carteFaceAvant;
    }

    public Carte getCarteFaceCachee() {
        return carteFaceCachee;
    }

    public void setCarteFaceCachee(Carte carteFaceCachee) {
        this.carteFaceCachee = carteFaceCachee;
    }

    public boolean isStatutOffre() {
        return statutOffre;
    }

    public void setStatutOffre(boolean statutOffre) {
        this.statutOffre = statutOffre;
    }
}
