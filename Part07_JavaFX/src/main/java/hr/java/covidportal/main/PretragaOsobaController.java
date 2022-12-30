package main.java.hr.java.covidportal.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Osoba;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaOsobaController implements Initializable {

    private static ObservableList<Osoba> observableListaOsoba;

    public static ObservableList<Osoba> getObservableListaOsoba() {
        return observableListaOsoba;
    }

    public static void setObservableListaOsoba(ObservableList<Osoba> observableListaOsoba) {
        PretragaOsobaController.observableListaOsoba = observableListaOsoba;
    }

    @FXML
    private TextField imeOsobe;

    @FXML
    private TextField prezimeOsobe;

    @FXML
    private TableView<Osoba> tablicaOsoba;

    @FXML
    private TableColumn<Osoba, String> imeOsobeStupac;

    @FXML
    private TableColumn<Osoba, String> prezimeOsobeStupac;

    @FXML
    private TableColumn<Osoba, Integer> starostOsobeStupac;

    @FXML
    private TableColumn<Osoba, String> zupanijaOsobeStupac;

    @FXML
    private TableColumn<Osoba, String> bolestOsobeStupac;

    @FXML
    private TableColumn<Osoba, String> kontaktiraneOsobeStupac;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        imeOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        prezimeOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        starostOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));
        zupanijaOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zupanija"));
        bolestOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zarazenBolescu"));
        kontaktiraneOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("kontaktiraneOsobe"));

        tablicaOsoba.setItems(observableListaOsoba);
    }

    public void pretrazi() {
        String ime = imeOsobe.getText().toUpperCase();
        String prezime = prezimeOsobe.getText().toUpperCase();
        List<Osoba> pomocLista = observableListaOsoba.stream()
                .filter(os -> (os.getIme().toUpperCase().contains(ime) && os.getPrezime().isEmpty()) ||
                        (os.getIme().toUpperCase().contains(ime) && os.getPrezime().toUpperCase().contains(prezime)) ||
                        (os.getIme().isEmpty() && os.getPrezime().toUpperCase().contains(prezime)))
                .collect(Collectors.toList());

        tablicaOsoba.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
