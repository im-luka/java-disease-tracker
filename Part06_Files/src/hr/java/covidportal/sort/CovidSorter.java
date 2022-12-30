package hr.java.covidportal.sort;

import hr.java.covidportal.model.Zupanija;

import java.util.Comparator;

/**
 * Služi kao uvjet sortiranja županija po broju zaraženih stanovnika.
 */
public class CovidSorter implements Comparator<Zupanija> {
    @Override
    public int compare(Zupanija zup1, Zupanija zup2) {
        double zup1Izracun = (double)zup1.getBrojZarazenih() / zup1.getBrojStanovnika();
        double zup2Izracun = (double)zup2.getBrojZarazenih() / zup2.getBrojStanovnika();
        if(zup1Izracun > zup2Izracun){
            return 1;
        }
        else if(zup1Izracun < zup2Izracun){
            return -1;
        }
        else{
            return 1;
        }
    }
}
