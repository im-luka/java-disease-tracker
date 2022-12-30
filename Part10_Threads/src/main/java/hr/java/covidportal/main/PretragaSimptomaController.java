package main.java.hr.java.covidportal.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.niti.SimptomiDohvatNit;

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

public class PretragaSimptomaController implements Initializable {

    private static ObservableList<Simptom> observableListaSimptoma;

    public static ObservableList<Simptom> getObservableListaSimptoma() {
        return observableListaSimptoma;
    }

    public static void setObservableListaSimptoma(ObservableList<Simptom> observableListaSimptoma) {
        PretragaSimptomaController.observableListaSimptoma = observableListaSimptoma;
    }

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private TableView<Simptom> tablicaSimptoma;

    @FXML
    private TableColumn<Simptom, String> nazivSimptomaStupac;

    @FXML
    private TableColumn<Simptom, String> vrijednostSimptomaStupac;

    /**
     * Učita sve simptome iz baze podataka, inicijalizira Controller i tablicu koja prikazuje sve unesene simptome u aplikaciji.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExecutorService servis = Executors.newCachedThreadPool();
        Future<List<Simptom>> listaSimptoma = servis.submit(new SimptomiDohvatNit());

        try {
            setObservableListaSimptoma(FXCollections.observableArrayList(listaSimptoma.get()));
        } catch (InterruptedException | ExecutionException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom dohvata podataka o simptomima iz baze podataka! ", iznimka);
        }

        nazivSimptomaStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        vrijednostSimptomaStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("vrijednost"));

        tablicaSimptoma.setItems(observableListaSimptoma);
    }

    /**
     * Omogućava korisniku unos punog naziva ili djela naziva simptoma te vrši pretraživanje po istom.
     */
    public void pretrazi() throws IOException, SQLException {
        String naziv = nazivSimptoma.getText().toUpperCase();
        List<Simptom> pomocLista = BazaPodataka.dohvatiSimptome().stream()
                .filter(simp -> simp.getNaziv().toUpperCase().contains(naziv))
                .collect(Collectors.toList());

        tablicaSimptoma.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
