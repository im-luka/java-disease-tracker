package main.java.hr.java.covidportal.main;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.model.Zupanija;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class PretragaZupanijaController implements Initializable {

    private static ObservableList<Zupanija> observableListaZupanija;

    public static ObservableList<Zupanija> getObservableListaZupanija() {
        return observableListaZupanija;
    }

    public static void setObservableListaZupanija(ObservableList<Zupanija> observableListaZupanija) {
        PretragaZupanijaController.observableListaZupanija = observableListaZupanija;
    }

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TableView<Zupanija> tablicaZupanija;

    @FXML
    private TableColumn<Zupanija, String> nazivZupanijeStupac;

    @FXML
    private TableColumn<Zupanija, Integer> brojStanovnikaZupanijeStupac;

    @FXML
    private TableColumn<Zupanija, Integer> brojZarazenihZupanijeStupac;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivZupanijeStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
        brojStanovnikaZupanijeStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
        brojZarazenihZupanijeStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));

        tablicaZupanija.setItems(observableListaZupanija);
    }

    public void pretrazi() {
        String naziv = nazivZupanije.getText().toUpperCase();
        List<Zupanija> pomocLista = observableListaZupanija.stream()
                .filter(zup -> zup.getNaziv().toUpperCase().contains(naziv))
                .collect(Collectors.toList());

        tablicaZupanija.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
