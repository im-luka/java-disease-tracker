package main.java.hr.java.covidportal.sort;

import main.java.hr.java.covidportal.model.Virus;

import java.util.Comparator;

/**
 * Sortira viruse po nazivu obrnutom od abecednog.
 */
public class VirusiKlinikaSorter implements Comparator<Virus> {
    @Override
    public int compare(Virus virus1, Virus virus2) {
        int usporedba = virus1.getNaziv().compareTo(virus2.getNaziv());
        if(usporedba < 0){
            return 1;
        }
        if(usporedba > 0){
            return -1;
        }
        else {
            return 0;
        }
    }
}
