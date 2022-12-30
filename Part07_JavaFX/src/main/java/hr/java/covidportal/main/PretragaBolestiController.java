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

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivBolestiStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("naziv"));
        nazivSimptomaBolestiStupac.setCellValueFactory(new PropertyValueFactory<Bolest, String>("simptomi"));

        List<Bolest> samoBolestiLista = observableListaBolesti.stream()
                .filter(bol -> !(bol instanceof Virus))
                .collect(Collectors.toList());
        observableListaBolesti = FXCollections.observableArrayList(samoBolestiLista);

        tablicaBolesti.setItems(observableListaBolesti);
    }

    public void pretrazi() {
        String naziv = nazivBolesti.getText().toUpperCase();
        List<Bolest> pomocLista = observableListaBolesti.stream()
                .filter(bol -> !(bol instanceof Virus) && bol.getNaziv().toUpperCase().contains(naziv))
                .collect(Collectors.toList());

        tablicaBolesti.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
