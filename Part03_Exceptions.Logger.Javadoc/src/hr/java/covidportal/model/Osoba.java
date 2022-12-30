package hr.java.covidportal.model;

/**
 * Služi za kreiranje instanci osobe koje su potrebne za ispravo funkcioniranje programa.
 */
public class Osoba {
    private String ime;
    private String prezime;
    private Integer starost;
    private Zupanija zupanija;
    private Bolest zarazenBolescu;
    private Osoba[] kontaktiraneOsobe;

    /**
     * Služi za pomoć kreiranja osobe korištenjem "Builder Patterna"
     */
    private Osoba() {

    }

    /**
     * Služi za lakše kreiranje osobe. Omogućava nam unos osobe bez da unosimo sva svojstva klase <code>Osoba</code>. U
     * slučaju da neko od svojstva ne unesemo, ne dolazi do greške. Također, u slučaju da za neko od svojstva nemamo
     * odgovarajuću vrijednost, ne moramo unositi null vrijednost (npr. ako nema kontaktiranih osoba
     * nećemo unositi null vrijednost nego ćemo unijeti osobu bez svojstva kontaktiraneOsobe).
     */
    public static class BuilderOsobe{
        private String ime;
        private String prezime;
        private Integer starost;
        private Zupanija zupanija;
        private Bolest zarazenBolescu;
        private Osoba[] kontaktiraneOsobe;

        public BuilderOsobe(String ime, String prezime){
            this.ime = ime;
            this.prezime = prezime;
        }

        public BuilderOsobe osStarost(Integer starost){
            this.starost = starost;
            return this;
        }

        public BuilderOsobe osZupanija(Zupanija zupanija){
            this.zupanija = zupanija;
            return this;
        }

        public BuilderOsobe osZarazenBolescu(Bolest zarazenBolescu){
            this.zarazenBolescu = zarazenBolescu;
            return this;
        }

        public BuilderOsobe osKontaktiraneOsobe(Osoba[] kontaktiraneOsobe){
            this.kontaktiraneOsobe = kontaktiraneOsobe;
            if(this.zarazenBolescu instanceof Virus){
                for(int i = 0; i < kontaktiraneOsobe.length; i++){
                    kontaktiraneOsobe[i].setZarazenBolescu(this.zarazenBolescu);
                }
            }
            return this;
        }

        public Osoba build(){
            Osoba osoba = new Osoba();
            osoba.ime = this.ime;
            osoba.prezime = this.prezime;
            osoba.starost = this.starost;
            osoba.zupanija = this.zupanija;
            osoba.zarazenBolescu = this.zarazenBolescu;
            osoba.kontaktiraneOsobe = this.kontaktiraneOsobe;
            return osoba;
        }
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public Integer getStarost() {
        return starost;
    }

    public void setStarost(Integer starost) {
        this.starost = starost;
    }

    public Zupanija getZupanija() {
        return zupanija;
    }

    public void setZupanija(Zupanija zupanija) {
        this.zupanija = zupanija;
    }

    public Bolest getZarazenBolescu() {
        return zarazenBolescu;
    }

    public void setZarazenBolescu(Bolest zarazenBolescu) {
        this.zarazenBolescu = zarazenBolescu;
    }

    public Osoba[] getKontaktiraneOsobe() {
        return kontaktiraneOsobe;
    }

    public void setKontaktiraneOsobe(Osoba[] kontaktiraneOsobe) {
        this.kontaktiraneOsobe = kontaktiraneOsobe;
    }
}
