package main.java.hr.java.covidportal.main;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Duration;
import main.java.hr.java.covidportal.model.Zupanija;
import main.java.hr.java.covidportal.niti.NajviseZarazenihNit;
import main.java.hr.java.covidportal.niti.ZupanijeDohvatNit;
import main.java.hr.java.covidportal.sort.CovidSorter;

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

public class PretragaZupanijaController implements Initializable {

    public static boolean prviPut = true;

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

    /**
     * Učita sve županije iz baze podataka, inicijalizira Controller i tablicu koja prikazuje sve unesene županije u aplikaciji.
     *
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ExecutorService servis = Executors.newCachedThreadPool();
        Future<List<Zupanija>> listaZupanija = servis.submit(new ZupanijeDohvatNit());

        try {
            setObservableListaZupanija(FXCollections.observableArrayList(listaZupanija.get()));
        } catch (InterruptedException | ExecutionException iznimka) {
            iznimka.printStackTrace();
            PocetniEkranController.logger.error("Greška prilikom dohvata podataka o županijama iz baze podataka! ", iznimka);
        }

        nazivZupanijeStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, String>("naziv"));
        brojStanovnikaZupanijeStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojStanovnika"));
        brojZarazenihZupanijeStupac.setCellValueFactory(new PropertyValueFactory<Zupanija, Integer>("brojZarazenih"));

        tablicaZupanija.setItems(observableListaZupanija);

        if(prviPut == true) {
            ExecutorService servis2 = Executors.newCachedThreadPool();
            servis2.execute(new NajviseZarazenihNit());
            prviPut = false;
        }

        Timeline ura = new Timeline(new KeyFrame(Duration.ZERO, xvar -> {
            try {
                Zupanija zup = BazaPodataka.dohvatiZupanije().stream().max(new CovidSorter()).get();
                double zarazenost = (double) zup.getBrojZarazenih() / zup.getBrojStanovnika() * 100;
                Main.mainStage.setTitle(zup.getNaziv() + " (" + zarazenost + "%)");
            } catch (SQLException | IOException iznimka) {
                iznimka.printStackTrace();
                PocetniEkranController.logger.error("Greška prilikom ispisa županije sa najvećim postotkom zaraženih u naslovu (headeru) aplikacije. ", iznimka);
            }
        }), new KeyFrame(Duration.seconds(10)));
        ura.play();
    }

    /**
     * Omogućava korisniku unos punog naziva ili djela naziva županije te vrši pretraživanje po istom.
     */
    public void pretrazi() throws IOException, SQLException {
        String naziv = nazivZupanije.getText().toUpperCase();
        List<Zupanija> pomocLista = BazaPodataka.dohvatiZupanije().stream()
                .filter(zup -> zup.getNaziv().toUpperCase().contains(naziv))
                .collect(Collectors.toList());

        tablicaZupanija.setItems(FXCollections.observableArrayList(pomocLista));
    }
}
