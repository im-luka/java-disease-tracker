package main.java.hr.java.covidportal.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Virus;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaVirusaController implements Initializable {

    private static ObservableList<Virus> observableListaVirusa;

    public static ObservableList<Virus> getObservableListaVirusa() {
        return observableListaVirusa;
    }

    public static void setObservableListaVirusa(ObservableList<Virus> observableListaVirusa) {
        PretragaVirusaController.observableListaVirusa = observableListaVirusa;
    }

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Virus> tablicaVirusa;

    @FXML
    private TableColumn<Virus, String> nazivVirusaStupac;

    @FXML
    private TableColumn<Virus, String> nazivSimptomaVirusaStupac;

    /**
     * Inicijalizira Controller i tablicu koja prikazuje sve unesene viruse u aplikaciji.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivVirusaStupac.setCellValueFactory(new PropertyValueFactory<Virus, String>("naziv"));
        nazivSimptomaVirusaStupac.setCellValueFactory(new PropertyValueFactory<Virus, String>("simptomi"));

        tablicaVirusa.setItems(observableListaVirusa);
    }

    /**
     * Omogućava korisniku unos punog naziva ili djela naziva virusa te vrši pretraživanje po istom.
     */
    public void pretrazi() {
        String naziv = nazivVirusa.getText().toUpperCase();
        List<Virus> pomocLista = observableListaVirusa.stream()
                .filter(vir -> vir.getNaziv().toUpperCase().contains(naziv))
                .collect(Collectors.toList());

        tablicaVirusa.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
