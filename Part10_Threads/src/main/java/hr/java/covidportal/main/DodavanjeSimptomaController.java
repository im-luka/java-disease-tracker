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
import main.java.hr.java.covidportal.niti.SimptomiDodajNit;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

    @FXML
    private RadioButton produktivni;

    @FXML
    private RadioButton intenzivno;

    @FXML
    private RadioButton visoka;

    @FXML
    private RadioButton jaka;

    /**
     * Služi za dodavanje novog simptoma preko korisničkog sučelja aplikacije u tablicu i bazu podataka.
     *
     * @throws IOException
     */
    public void dodajSimptom() throws IOException {
        try{
            String dodajNazivSimptoma = nazivSimptoma.getText();
            RadioButton odabranaVrijednost = (RadioButton) vrijednostSimptoma.getSelectedToggle();
            String dodajVrijednostSimptoma = odabranaVrijednost.getText();
            VrijednostiSimptoma vr = VrijednostiSimptoma.valueOf(dodajVrijednostSimptoma);

            Integer velicina = BazaPodataka.dohvatiSimptome().size() + 1;
            Simptom simpt = new Simptom((long)velicina, dodajNazivSimptoma, vr.getVrijednost());

            ExecutorService servis = Executors.newCachedThreadPool();
            servis.execute(new SimptomiDodajNit(simpt));

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
        catch (RuntimeException | SQLException iznimka) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje simptoma");
            alert.setHeaderText("Greška kod dodavanja simptoma!");
            alert.showAndWait();
            povratak();
        }
    }

    /**
     * Služi za povratak na početni ekran za pretragu simptoma.
     *
     * @throws IOException
     */
    public void povratak() throws IOException {
        Parent povratakFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaSimptoma.fxml"));
        Scene povratakScene = new Scene(povratakFrame);
        Main.getMainStage().setScene(povratakScene);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
