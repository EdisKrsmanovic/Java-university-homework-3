package ba.unsa.etf.rpr.t7;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PretragaController {
    public Korisnik korisnik;

    public ScrollPane scrImgPane;
    public TextField textSearch;

    private String imageUrl = null;

    @FXML
    public void initialize() {
        Text value = new Text("blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla");
        value.setWrappingWidth(500);
        scrImgPane.setContent(value);
        scrImgPane.setFitToWidth(true);
    }

    public void searchAction() {
        //threading, na svaku novu sliku staviti listener i ako se klikne staviti imageurl na nju, te skinuti sliku u img folder?
    }

    public void okAction() {
        if(imageUrl == null || imageUrl.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nijedna slika nije izabrana");
            alert.setHeaderText("Niste izabrali sliku koju želite");
            alert.setContentText("Unesite pretragu a zatim izaberite sliku, ili kliknite na Cancel ako ne zelite novu sliku");
            alert.showAndWait();
        }
        else {
            korisnik.setSlika(imageUrl);
        }
    }

    public void cancelAction() {

        //treba ponistiti promjene i da se slika ne promijeni

        Stage stage = (Stage) textSearch.getScene().getWindow();
        stage.close();
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
