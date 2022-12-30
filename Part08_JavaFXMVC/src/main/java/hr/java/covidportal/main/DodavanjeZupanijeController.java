package main.java.hr.java.covidportal.main;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import main.java.hr.java.covidportal.model.Zupanija;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ResourceBundle;

public class DodavanjeZupanijeController implements Initializable {

    @FXML
    private TextField nazivZupanije;

    @FXML
    private TextField brojStanovnikaZupanije;

    @FXML
    private TextField brojZarazenihZupanije;

    public void dodajZupaniju() throws IOException {
        try{
            String dodajNazivZupanije = nazivZupanije.getText();
            Integer dodajBrojStanovnikaZupanije = Integer.valueOf(brojStanovnikaZupanije.getText());
            Integer dodajBrojZarazenihZupanije = Integer.valueOf(brojZarazenihZupanije.getText());

            Integer velicina = PretragaZupanijaController.getObservableListaZupanija().size() + 1;
            Zupanija zup = new Zupanija((long)velicina, dodajNazivZupanije, dodajBrojStanovnikaZupanije, dodajBrojZarazenihZupanije);

            PretragaZupanijaController.getObservableListaZupanija().add(zup);

            Path zapis = Path.of("dat/zupanije.txt");
            try {
                Files.writeString(zapis, "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, zup.getId() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, zup.getNaziv() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, zup.getBrojStanovnika() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, String.valueOf(zup.getBrojZarazenih()), StandardOpenOption.APPEND);
            }
            catch (IOException iznimka) {
                PocetniEkranController.logger.error("Greška kod pisanja u datoteku županije! " + iznimka);
            }

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Dodavanje županije");
            alert.setHeaderText("Uspješno dodana županija!");
            alert.setContentText("Uspješno si dodao županiju " + dodajNazivZupanije + "!!");
            alert.showAndWait();
            povratak();
        }
        catch (RuntimeException iznimka) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje županije");
            alert.setHeaderText("Greška kod dodavanja županije!");
            alert.setContentText("Prilikom unosa broja stanovnika i zaraženih, molim unesi Integer vrijednost!");
            alert.showAndWait();
            povratak();
        }

    }

    public void povratak() throws IOException {
        Parent povratakFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaZupanija.fxml"));
        Scene povratakScene = new Scene(povratakFrame);
        Main.getMainStage().setScene(povratakScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
