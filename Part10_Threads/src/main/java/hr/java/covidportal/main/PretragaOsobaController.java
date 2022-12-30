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
import main.java.hr.java.covidportal.niti.OsobeDohvatNit;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
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

    /**
     * Učita sve osobe iz baze podataka, inicijalizira Controller i tablicu koja prikazuje sve unesene osobe u aplikaciji.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExecutorService servis = Executors.newCachedThreadPool();
        Future<List<Osoba>> listaOsoba = servis.submit(new OsobeDohvatNit());

        try {
            setObservableListaOsoba(FXCollections.observableArrayList(listaOsoba.get()));
        } catch (InterruptedException | ExecutionException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom dohvata podataka o osobi iz baze podataka! ", iznimka);
        }

        imeOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("ime"));
        prezimeOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("prezime"));
        starostOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, Integer>("starost"));
        zupanijaOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zupanija"));
        bolestOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("zarazenBolescu"));
        kontaktiraneOsobeStupac.setCellValueFactory(new PropertyValueFactory<Osoba, String>("kontaktiraneOsobe"));

        tablicaOsoba.setItems(observableListaOsoba);
    }

    /**
     * Omogućava korisniku unos punog imena/prezimena ili djela imena/prezimena osobe te vrši pretraživanje po istom.
     */
    public void pretrazi() throws IOException, SQLException {
        String ime = imeOsobe.getText().toUpperCase();
        String prezime = prezimeOsobe.getText().toUpperCase();
        List<Osoba> pomocLista = BazaPodataka.dohvatiOsobe().stream()
                .filter(os -> (os.getIme().toUpperCase().contains(ime) && os.getPrezime().isEmpty()) ||
                        (os.getIme().toUpperCase().contains(ime) && os.getPrezime().toUpperCase().contains(prezime)) ||
                        (os.getIme().isEmpty() && os.getPrezime().toUpperCase().contains(prezime)))
                .collect(Collectors.toList());

        tablicaOsoba.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
