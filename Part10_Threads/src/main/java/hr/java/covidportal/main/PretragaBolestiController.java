package main.java.hr.java.covidportal.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.niti.BolestiDohvatNit;

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

public class PretragaBolestiController implements Initializable {

    private static ObservableList<Bolest> observableListaBolesti;

    public static ObservableList<Bolest> getObservableListaBolesti() {
        return observableListaBolesti;
    }

    public static void setObservableListaBolesti(ObservableList<Bolest> observableListaBolesti) {
        PretragaBolestiController.observableListaBolesti = observableListaBolesti;
    }

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TableView<Bolest> tablicaBolesti;

    @FXML
    private TableColumn<Bolest, String> nazivBolestiStupac;

    @FXML
    private TableColumn<Bolest, String> nazivSimptomaBolestiStupac;

    @FXML
    private TableColumn<Bolest, Boolean> jeLiVirusStupac;

    /**
     * Učita sve bolesti iz baze podataka, inicijalizira Controller i tablicu koja prikazuje sve unesene bolesti u aplikaciji.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExecutorService servis = Executors.newCachedThreadPool();
        Future<List<Bolest>> listaBolesti = servis.submit(new BolestiDohvatNit());

        try {
            setObservableListaBolesti(FXCollections.observableArrayList(listaBolesti.get()));
        }
        catch (InterruptedException | ExecutionException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom dohvata podataka o bolesti iz baze podataka! ", iznimka);
        }

        nazivBolestiStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        nazivSimptomaBolestiStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("simptomi"));
        jeLiVirusStupac.setCellValueFactory(new PropertyValueFactory<Bolest, Boolean>("jeLiVirus"));

        tablicaBolesti.setItems(observableListaBolesti);
    }

    /**
     * Omogućava korisniku unos punog naziva ili djela naziva bolesti te vrši pretraživanje po istom.
     */
    public void pretrazi() throws IOException, SQLException {
        String naziv = nazivBolesti.getText().toUpperCase();
        List<Bolest> pomocLista = BazaPodataka.dohvatiBolestVirus().stream()
                .filter(bol ->  bol.getNaziv().toUpperCase().contains(naziv))
                .collect(Collectors.toList());

        tablicaBolesti.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
