package main.java.hr.java.covidportal.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import main.java.hr.java.covidportal.enums.VrijednostiSimptoma;
import main.java.hr.java.covidportal.model.Simptom;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class DodavanjeSimptomaController implements Initializable {

    @FXML
    private TextField nazivSimptoma;

    @FXML
    private ToggleGroup vrijednostSimptoma;

    @FXML
    private RadioButton cesto;

    @FXML
    private RadioButton srednje;

    @FXML
    private RadioButton rijetko;

    public void dodajSimptom() throws IOException {
        try{
            String dodajNazivSimptoma = nazivSimptoma.getText();
            RadioButton odabranaVrijednost = (RadioButton) vrijednostSimptoma.getSelectedToggle();
            String dodajVrijednostSimptoma = odabranaVrijednost.getText();
            VrijednostiSimptoma.valueOf(dodajVrijednostSimptoma);

            Integer velicina = PretragaSimptomaController.getObservableListaSimptoma().size() + 1;
            Simptom simpt = new Simptom((long)velicina, dodajNazivSimptoma, dodajVrijednostSimptoma);

            PretragaSimptomaController.getObservableListaSimptoma().add(simpt);

            Path zapis = Path.of("dat/simptomi.txt");
            try {
                Files.writeString(zapis, "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, simpt.getId() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, simpt.getNaziv() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, simpt.getVrijednost(), StandardOpenOption.APPEND);
            }
            catch (IOException iznimka) {
                PocetniEkranController.logger.error("Greška kod pisanja u datoteku simptomi! " + iznimka);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje simptoma");
            alert.setHeaderText("Uspješno dodan simptom!");
            alert.setContentText("Uspješno si dodao simptom " + dodajNazivSimptoma + "!!");
            alert.showAndWait();
            povratak();
        }
        catch (RuntimeException iznimka) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje simptoma");
            alert.setHeaderText("Greška kod dodavanja simptoma!");
            alert.showAndWait();
            povratak();
        }
    }

    public void povratak() throws IOException {
        Parent povratakFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene povratakScene = new Scene(povratakFrame);
        Main.getMainStage().setScene(povratakScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
