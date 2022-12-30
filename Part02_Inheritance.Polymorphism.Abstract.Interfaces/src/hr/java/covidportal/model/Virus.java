package hr.java.covidportal.model;

public class Virus extends Bolest implements Zarazno{

    public Virus(String naziv, Simptom[] simptomi) {
        super(naziv, simptomi);
    }

    @Override
    public void prelazakZarazeNaOsobu(Osoba osoba) {
        osoba.setZarazenBolescu(this);
    }
}
