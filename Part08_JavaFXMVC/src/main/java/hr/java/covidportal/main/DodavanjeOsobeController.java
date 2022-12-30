package main.java.hr.java.covidportal.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.iznimke.DuplikatKontaktiraneOsobe;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Osoba;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DodavanjeOsobeController implements Initializable {

    private List<Osoba> pomocnaListaOsoba = new ArrayList<>();

    @FXML
    private TextField imeOsobe;

    @FXML
    private TextField prezimeOsobe;

    @FXML
    private TextField starostOsobe;

    @FXML
    private ChoiceBox<Zupanija> zupanijaOsobe;

    @FXML
    private ChoiceBox<Bolest> bolestOsobe;

    @FXML
    private TableView<Osoba> tablicaKontaktiranihOsobe;

    @FXML
    private TableColumn<Osoba, String> imeKontaktiraneOsobeStupac;

    @FXML
    private TableColumn<Osoba, String> prezimeKontaktiraneOsobeStupac;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imeKontaktiraneOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        prezimeKontaktiraneOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        tablicaKontaktiranihOsobe.setItems(PretragaOsobaController.getObservableListaOsoba());
        zupanijaOsobe.setItems(PretragaZupanijaController.getObservableListaZupanija());
        bolestOsobe.setItems(PretragaBolestiController.getObservableListaBolesti());

        tablicaKontaktiranihOsobe.setRowFactory(tabla -> {
            TableRow<Osoba> redak = new TableRow<>();
            redak.setOnMouseClicked(klik -> {
                if(klik.getClickCount() == 2 && !(redak.isEmpty())){
                    try {
                        Osoba os = redak.getItem();
                        provjeraKontaktiraneOsobe(os, pomocnaListaOsoba);
                        pomocnaListaOsoba.add(os);
                        redak.setStyle("-fx-selection-bar: midnightblue;");
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setHeaderText("Uspješno dodana kontaktirana osoba " + os.getIme() + " " + os.getPrezime() + " za osobu " + imeOsobe.getText() + " " + prezimeOsobe.getText() + "!");
                        alert.setContentText("Ukoliko postoji još kontaktiranih osoba, molim odaberi. U suprotnom pritisni gumb Dodaj za dodavanje osobe.");
                        alert.showAndWait();
                    }
                    catch (DuplikatKontaktiraneOsobe iznimka) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Dodavanje kontaktirane osobe");
                        alert.setHeaderText("Greška kod dodavanja kontaktirane osobe!");
                        alert.setContentText("Već si dodao ovu osobu kao kontaktiranu osobu!");
                        alert.showAndWait();
                        try {
                            povratak();
                        }
                        catch (IOException iznimka2) {
                            PocetniEkranController.logger.error(String.valueOf(iznimka2));
                        }
                    }
                }
            });
            return redak;
        });
    }

    public void dodajOsobu() throws IOException {
        try {
            String ime = imeOsobe.getText();
            String prezime = prezimeOsobe.getText();
            Integer starost = Integer.parseInt(starostOsobe.getText());
            Zupanija zup = zupanijaOsobe.getValue();
            Bolest bol = bolestOsobe.getValue();

            Integer velicina = PretragaOsobaController.getObservableListaOsoba().size() + 1;

            Osoba os;
            if (pomocnaListaOsoba.size() == 0) {
                os = new Osoba.BuilderOsobe((long)velicina, ime, prezime)
                        .osStarost(starost)
                        .osZupanija(zup)
                        .osZarazenBolescu(bol)
                        .build();
            }
            else {
                os = new Osoba.BuilderOsobe((long)velicina, ime, prezime)
                        .osStarost(starost)
                        .osZupanija(zup)
                        .osZarazenBolescu(bol)
                        .osKontaktiraneOsobe(pomocnaListaOsoba)
                        .build();
            }
            PretragaOsobaController.getObservableListaOsoba().add(os);

            Path zapis = Path.of("dat/osobe.txt");
            try {
                Files.writeString(zapis, "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, os.getId() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, os.getIme() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, os.getPrezime() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, os.getStarost() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, zup.getId() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, bol.getId() + "\n", StandardOpenOption.APPEND);
                if(pomocnaListaOsoba.size() == 0){
                    Files.writeString(zapis, "0", StandardOpenOption.APPEND);
                }
                else {
                    for (int i = 0; i < pomocnaListaOsoba.size(); i++) {
                        if (i == pomocnaListaOsoba.size() - 1){
                            Files.writeString(zapis, String.valueOf(pomocnaListaOsoba.get(i).getId()), StandardOpenOption.APPEND);
                        }
                        else {
                            Files.writeString(zapis, pomocnaListaOsoba.get(i).getId() + ",", StandardOpenOption.APPEND);
                        }
                    }
                }
            }
            catch (IOException iznimka){
                PocetniEkranController.logger.error("Greška kod pisanja u datoteku osobe! " + iznimka);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje osobe");
            alert.setHeaderText("Uspješno dodana osoba!");
            alert.setContentText("Uspješno si dodao osobu " + ime + " " + prezime + "!!");
            alert.showAndWait();
            povratak();
        }
        catch (RuntimeException iznimka) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje virusa");
            alert.setHeaderText("Greška kod dodavanja virusa!");
            alert.showAndWait();
            povratak();
        }
    }

    public void povratak() throws IOException {
        Parent povratakFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaOsoba.fxml"));
        Scene povratakScene = new Scene(povratakFrame);
        Main.getMainStage().setScene(povratakScene);
    }

    public void provjeraKontaktiraneOsobe (Osoba osoba, List<Osoba> pomocnaListaOsoba) throws DuplikatKontaktiraneOsobe {
        for(Osoba os : pomocnaListaOsoba) {
            if(os.getId() == osoba.getId()) {
                throw new DuplikatKontaktiraneOsobe();
            }
        }
    }
}
