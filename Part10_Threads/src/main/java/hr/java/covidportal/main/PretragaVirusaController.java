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
import main.java.hr.java.covidportal.model.Virus;
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

public class PretragaVirusaController implements Initializable {

    private static ObservableList<Bolest> observableListaVirusa;

    public static ObservableList<Bolest> getObservableListaVirusa() {
        return observableListaVirusa;
    }

    public static void setObservableListaVirusa(ObservableList<Bolest> observableListaVirusa) {
        PretragaVirusaController.observableListaVirusa = observableListaVirusa;
    }

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Bolest> tablicaVirusa;

    @FXML
    private TableColumn<Virus, String> nazivVirusaStupac;

    @FXML
    private TableColumn<Virus, String> nazivSimptomaVirusaStupac;

    /**
     * Učita sve viruse iz baze podataka, inicijalizira Controller i tablicu koja prikazuje sve unesene viruse u aplikaciji.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExecutorService servis = Executors.newCachedThreadPool();
        Future<List<Bolest>> listaVirusa = servis.submit(new BolestiDohvatNit());

        try {
            setObservableListaVirusa(FXCollections.observableArrayList(listaVirusa.get().stream()
                    .filter(vir -> vir.isJeLiVirus() == true)
                    .collect(Collectors.toList())));
        }
        catch (InterruptedException | ExecutionException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom dohvata podataka o virusu iz baze podataka! ", iznimka);
        }

        nazivVirusaStupac.setCellValueFactory(new PropertyValueFactory<Virus, String>("naziv"));
        nazivSimptomaVirusaStupac.setCellValueFactory(new PropertyValueFactory<Virus, String>("simptomi"));

        tablicaVirusa.setItems(observableListaVirusa);
    }

    /**
     * Omogućava korisniku unos punog naziva ili djela naziva virusa te vrši pretraživanje po istom.
     */
    public void pretrazi() throws IOException, SQLException {
        String naziv = nazivVirusa.getText().toUpperCase();
        List<Bolest> pomocLista = BazaPodataka.dohvatiBolestVirus().stream()
                .filter(vir -> vir.getNaziv().toUpperCase().contains(naziv) && vir.isJeLiVirus())
                .collect(Collectors.toList());

        tablicaVirusa.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
