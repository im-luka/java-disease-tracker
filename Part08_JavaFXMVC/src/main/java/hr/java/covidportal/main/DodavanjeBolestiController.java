package main.java.hr.java.covidportal.main;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.iznimke.BolestIstihSimptoma;
import main.java.hr.java.covidportal.model.Bolest;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DodavanjeBolestiController implements Initializable {

    private List<Simptom> pomocnaListaSimptoma = new ArrayList<>();

    @FXML
    private TextField nazivBolesti;

    @FXML
    private TableView<Simptom> tablicaSimptomaBolesti;

    @FXML
    private TableColumn<Simptom, String> nazivSimptomaBolestiStupac;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivSimptomaBolestiStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        tablicaSimptomaBolesti.setItems(PretragaSimptomaController.getObservableListaSimptoma());

        tablicaSimptomaBolesti.setRowFactory(tabla -> {
            TableRow<Simptom> redak = new TableRow<>();
            redak.setOnMouseClicked(klik -> {
                if(klik.getClickCount() == 2 && !(redak.isEmpty())){
                    Simptom simpt = redak.getItem();
                    pomocnaListaSimptoma.add(simpt);
                    redak.setStyle("-fx-selection-bar: midnightblue;");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Uspješno dodan simptom " + simpt.getNaziv() + " za bolest " + nazivBolesti.getText() + "!");
                    alert.setContentText("Ukoliko postoji još koji simptom, molim odaberi. U suprotnom pritisni gumb Dodaj za dodavanje bolesti.");
                    alert.showAndWait();
                }
            });
            return redak;
        });
    }

    public void dodajBolest() throws IOException {
        try {
            provjeraSimptoma(pomocnaListaSimptoma);

            String naziv = nazivBolesti.getText();
            Integer velicina = PretragaBolestiController.getObservableListaBolesti().size() + 1;
            Integer velicina2 = PretragaBolestiController.getObservableListaSamoBolesti().size() + 1;

            Bolest bol = new Bolest((long)velicina, naziv, pomocnaListaSimptoma);
            Bolest bol2 = new Bolest((long)velicina2, naziv, pomocnaListaSimptoma);

            PretragaBolestiController.getObservableListaBolesti().add(bol);
            PretragaBolestiController.getObservableListaSamoBolesti().add(bol2);

            Path zapis = Path.of("dat/bolesti.txt");
            try {
                Files.writeString(zapis, "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, bol2.getId() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, bol2.getNaziv() + "\n", StandardOpenOption.APPEND);
                for (int i = 0; i < pomocnaListaSimptoma.size(); i++){
                    if (i == pomocnaListaSimptoma.size() - 1){
                        Files.writeString(zapis, String.valueOf(pomocnaListaSimptoma.get(i).getId()), StandardOpenOption.APPEND);
                    }
                    else {
                        Files.writeString(zapis, pomocnaListaSimptoma.get(i).getId() + ",", StandardOpenOption.APPEND);
                    }
                }
            }
            catch (IOException iznimka) {
                PocetniEkranController.logger.error("Greška kod pisanja u datoteku bolesti! " + iznimka);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje bolesti");
            alert.setHeaderText("Uspješno dodana bolesti!");
            alert.setContentText("Uspješno si dodao bolest " + naziv + "!!");
            alert.showAndWait();
            povratak();
        }
        catch (BolestIstihSimptoma iznimka1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje bolesti");
            alert.setHeaderText("Greška kod dodavanja simptoma bolesti!");
            alert.setContentText("Već si dodao bolest sa istim simptomima!");
            alert.showAndWait();
            povratak();
        }
        catch (RuntimeException iznimka2) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje bolesti");
            alert.setHeaderText("Greška kod dodavanja bolesti!");
            alert.showAndWait();
            povratak();
        }
    }

    public void povratak() throws IOException {
        Parent povratakFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaBolesti.fxml"));
        Scene povratakScene = new Scene(povratakFrame);
        Main.getMainStage().setScene(povratakScene);
    }

    public void provjeraSimptoma(List<Simptom> pomocnaListaSimptoma) throws BolestIstihSimptoma {
        List<Bolest> pomocnaListaBolesti = PretragaBolestiController.getObservableListaSamoBolesti().stream().collect(Collectors.toList());
        int brojac = 0;
        if(pomocnaListaSimptoma.size() == 1) {
            for(Bolest bol : pomocnaListaBolesti) {
                if(bol.getSimptomi().size() == 1){
                    if(bol.getSimptomi().get(0).getId() == pomocnaListaSimptoma.get(0).getId()){
                        throw new BolestIstihSimptoma();
                    }
                }
            }
        }
        else {
            for(Bolest bol : pomocnaListaBolesti) {
                brojac = 0;
                if(bol.getSimptomi().size() == pomocnaListaSimptoma.size()) {
                    for (Simptom simpBol : bol.getSimptomi()) {
                        for (Simptom simpSimp : pomocnaListaSimptoma) {
                            if (simpBol.getId() == simpSimp.getId()) {
                                brojac++;
                            }
                        }
                    }
                }
                if(brojac == pomocnaListaSimptoma.size()){
                    throw new BolestIstihSimptoma();
                }
            }
        }
    }
}
