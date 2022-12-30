package main.java.hr.java.covidportal.main;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.hr.java.covidportal.iznimke.BolestIstihSimptoma;
import main.java.hr.java.covidportal.model.Simptom;
import main.java.hr.java.covidportal.model.Virus;
import main.java.hr.java.covidportal.niti.BolestiDodajNit;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class DodavanjeVirusaController implements Initializable {

    private List<Simptom> pomocnaListaSimptoma = new ArrayList<>();

    @FXML
    private TextField nazivVirusa;

    @FXML
    private TableView<Simptom> tablicaSimptomaVirusa;

    @FXML
    private TableColumn<Simptom, String> nazivSimptomaVirusaStupac;

    /**
     * Inicijalizira Controller i tablicu koja prikazuje sve unesene simptome u aplikaciji kako bi ih se pridružilo virusu
     * koji se unosi.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nazivSimptomaVirusaStupac.setCellValueFactory(new PropertyValueFactory<Simptom, String>("naziv"));
        try {
            tablicaSimptomaVirusa.setItems(FXCollections.observableArrayList(BazaPodataka.dohvatiSimptome()));
        } catch (SQLException | IOException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom dohvata podataka o simptomima iz baze podataka kod dodavanja virusa! ", iznimka);
        }

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

    /**
     * Služi za dodavanje novog virusa preko korisničkog sučelja aplikacije u tablicu i bazu podataka.
     *
     * @throws IOException
     */
    public void dodajVirus() throws IOException {
        try {
            DodavanjeBolestiController.provjeraSimptoma(pomocnaListaSimptoma);

            String naziv = nazivVirusa.getText();
            long velicina = BazaPodataka.dohvatiBolestVirus().stream().filter(vir -> vir instanceof Virus).count() + 1;

            Virus vir = new Virus(velicina, naziv, true, pomocnaListaSimptoma);

            ExecutorService servis = Executors.newCachedThreadPool();
            servis.execute(new BolestiDodajNit(vir, true));

            Path zapis = Path.of("dat/virusi.txt");
            try {
                Files.writeString(zapis, "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, vir.getId() + "\n", StandardOpenOption.APPEND);
                Files.writeString(zapis, vir.getNaziv() + "\n", StandardOpenOption.APPEND);
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
            alert.setTitle("Dodavanje bolesti/virusa");
            alert.setHeaderText("Greška kod dodavanja simptoma bolesti/virusa!");
            alert.setContentText("Već si dodao bolest/virus sa istim simptomima!");
            alert.showAndWait();
            povratak();
        }
        catch (RuntimeException | SQLException iznimka) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Dodavanje virusa");
            alert.setHeaderText("Greška kod dodavanja virusa!");
            alert.showAndWait();
            povratak();
        }
    }

    /**
     * Služi za povratak na početni ekran za pretragu virusa.
     *
     * @throws IOException
     */
    public void povratak() throws IOException {
        Parent povratakFrame = FXMLLoader.load(getClass().getClassLoader().getResource("pretragaVirusa.fxml"));
        Scene povratakScene = new Scene(povratakFrame);
        Main.getMainStage().setScene(povratakScene);
    }
}
