package main.java.hr.java.covidportal.main;

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
import main.java.hr.java.covidportal.model.Virus;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class DodavanjeVirusaController implements Initializable {

    private List<Simptom> pomocnaListaSimptoma = new ArrayList<>();

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Simptom> tablicaSimptomaVirusa;

    @FXML
    private TableColumn<Simptom, String> nazivSimptomaVirusaStupac;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivSimptomaVirusaStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        tablicaSimptomaVirusa.setItems(PretragaSimptomaController.getObservableListaSimptoma());

        tablicaSimptomaVirusa.setRowFactory(tabla -> {
            TableRow<Simptom> redak = new TableRow<>();
            redak.setOnMouseClicked(klik -> {
                if(klik.getClickCount() == 2 && !(redak.isEmpty())){
                    Simptom simpt = redak.getItem();
                    pomocnaListaSimptoma.add(simpt);
                    redak.setStyle("-fx-selection-bar: midnightblue;");
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setHeaderText("Uspješno dodan simptom " + simpt.getNaziv() + " za bolest " + nazivVirusa.getText() + "!");
                    alert.setContentText("Ukoliko postoji još koji simptom, molim odaberi. U suprotnom pritisni gumb Dodaj za dodavanje bolesti.");
                    alert.showAndWait();
                }
            });
            return redak;
        });
    }

    public void dodajVirus() throws IOException {
        try {
            provjeraSimptoma(pomocnaListaSimptoma);

            String naziv = nazivVirusa.getText();
            Integer velicina = PretragaBolestiController.getObservableListaBolesti().size() + 1;
            Integer velicina2 = PretragaVirusaController.getObservableListaVirusa().size() + 1;

            Virus vir = new Virus((long)velicina, naziv, pomocnaListaSimptoma);
            Virus vir2 = new Virus((long)velicina2, naziv, pomocnaListaSimptoma);

            PretragaBolestiController.getObservableListaBolesti().add(vir);
            PretragaVirusaController.getObservableListaVirusa().add(vir2);

            Path zapis = Path.of("dat/virusi.txt");
            try {
                Files.writeString(zapis, "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, vir2.getId() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, vir2.getNaziv() + "\n", StandardOpenOption.APPEND);
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
                PocetniEkranController.logger.error("Greška kod pisanja u datoteku virusi! " + iznimka);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje virusa");
            alert.setHeaderText("Uspješno dodan virus!");
            alert.setContentText("Uspješno si dodao virus " + naziv + "!!");
            alert.showAndWait();
            povratak();
        }
        catch (BolestIstihSimptoma iznimka1) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje virusa");
            alert.setHeaderText("Greška kod dodavanja simptoma virusa!");
            alert.setContentText("Već si dodao virus sa istim simptomima!");
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
        Parent povratakFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene povratakScene = new Scene(povratakFrame);
        Main.getMainStage().setScene(povratakScene);
    }

    public void provjeraSimptoma(List<Simptom> pomocnaListaSimptoma) throws BolestIstihSimptoma {
        List<Virus> pomocnaListaVirusa = PretragaVirusaController.getObservableListaVirusa().stream().collect(Collectors.toList());
        int brojac = 0;
        if(pomocnaListaSimptoma.size() == 1) {
            for(Virus vir : pomocnaListaVirusa) {
                if(vir.getSimptomi().size() == 1) {
                    if (vir.getSimptomi().get(0).getId() == pomocnaListaSimptoma.get(0).getId()) {
                        throw new BolestIstihSimptoma();
                    }
                }
            }
        }
        else {
            for(Virus vir : pomocnaListaVirusa) {
                brojac = 0;
                if(vir.getSimptomi().size() == pomocnaListaSimptoma.size()) {
                    for (Simptom simpVir : vir.getSimptomi()) {
                        for (Simptom simpSimp : pomocnaListaSimptoma) {
                            if (simpVir.getId() == simpSimp.getId()) {
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
