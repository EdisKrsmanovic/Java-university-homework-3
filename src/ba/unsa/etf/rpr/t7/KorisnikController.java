package ba.unsa.etf.rpr.t7;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Group;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Locale;
import java.util.ResourceBundle;

public class KorisnikController {
    public TextField fldIme;
    public TextField fldPrezime;
    public TextField fldEmail;
    public TextField fldUsername;
    public ListView<Korisnik> listKorisnici;
    public PasswordField fldPassword;

    public Button btnObrisi;
    public Button btnDodaj;
    public Button btnKraj;

    public Menu menuFile;
    public Menu menuHelp;
    public MenuItem fileSave;
    public MenuItem filePrint;
    public MenuItem fileLanguage;
    public MenuItem fileExit;
    public MenuItem helpAbout;

    public Label labelName;
    public Label labelSurname;
    public Label labelEmail;
    public Label labelUsername;
    public Label labelPassword;

    private KorisniciModel model;

    public KorisnikController(KorisniciModel model) {
        this.model = model;
    }

    @FXML
    public void initialize() {
        listKorisnici.setItems(model.getKorisnici());
        listKorisnici.getSelectionModel().selectedItemProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            model.setTrenutniKorisnik(newKorisnik);
            listKorisnici.refresh();
        });

        model.trenutniKorisnikProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            if (oldKorisnik != null) {
                fldIme.textProperty().unbindBidirectional(oldKorisnik.imeProperty());
                fldPrezime.textProperty().unbindBidirectional(oldKorisnik.prezimeProperty());
                fldEmail.textProperty().unbindBidirectional(oldKorisnik.emailProperty());
                fldUsername.textProperty().unbindBidirectional(oldKorisnik.usernameProperty());
                fldPassword.textProperty().unbindBidirectional(oldKorisnik.passwordProperty());
            }
            if (newKorisnik == null) {
                fldIme.setText("");
                fldPrezime.setText("");
                fldEmail.setText("");
                fldUsername.setText("");
                fldPassword.setText("");
            } else {
                fldIme.textProperty().bindBidirectional(newKorisnik.imeProperty());
                fldPrezime.textProperty().bindBidirectional(newKorisnik.prezimeProperty());
                fldEmail.textProperty().bindBidirectional(newKorisnik.emailProperty());
                fldUsername.textProperty().bindBidirectional(newKorisnik.usernameProperty());
                fldPassword.textProperty().bindBidirectional(newKorisnik.passwordProperty());
            }
        });

        fldIme.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldIme.getStyleClass().removeAll("poljeNijeIspravno");
                fldIme.getStyleClass().add("poljeIspravno");
            } else {
                fldIme.getStyleClass().removeAll("poljeIspravno");
                fldIme.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPrezime.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldPrezime.getStyleClass().removeAll("poljeNijeIspravno");
                fldPrezime.getStyleClass().add("poljeIspravno");
            } else {
                fldPrezime.getStyleClass().removeAll("poljeIspravno");
                fldPrezime.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldEmail.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldEmail.getStyleClass().removeAll("poljeNijeIspravno");
                fldEmail.getStyleClass().add("poljeIspravno");
            } else {
                fldEmail.getStyleClass().removeAll("poljeIspravno");
                fldEmail.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldUsername.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldUsername.getStyleClass().removeAll("poljeNijeIspravno");
                fldUsername.getStyleClass().add("poljeIspravno");
            } else {
                fldUsername.getStyleClass().removeAll("poljeIspravno");
                fldUsername.getStyleClass().add("poljeNijeIspravno");
            }
        });

        fldPassword.textProperty().addListener((obs, oldIme, newIme) -> {
            if (!newIme.isEmpty()) {
                fldPassword.getStyleClass().removeAll("poljeNijeIspravno");
                fldPassword.getStyleClass().add("poljeIspravno");
            } else {
                fldPassword.getStyleClass().removeAll("poljeIspravno");
                fldPassword.getStyleClass().add("poljeNijeIspravno");
            }
        });
    }

    public void dodajAction(ActionEvent actionEvent) {
        model.getKorisnici().add(new Korisnik("", "", "", "", ""));
        listKorisnici.getSelectionModel().selectLast();
    }

    public void krajAction(ActionEvent actionEvent) {
        System.exit(0);
    }

    public void obrisiAction(ActionEvent actionEvent) {
        model.obrisiTrenutnog();
        listKorisnici.refresh();
    }

    public void exitAction(ActionEvent actionEvent) {
        Platform.exit();
    }

    public void aboutAction(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informacije");
        alert.setHeaderText("Informacije o aplikaciji");
        Hyperlink hyperlink = new Hyperlink("https://github.com/RPR-2019/zadaca-3-EdisKrsmanovic\n");
        hyperlink.setOnAction(e -> {
            try {
                new ProcessBuilder("x-www-browser", "https://github.com/RPR-2019/zadaca-3-EdisKrsmanovic").start();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        });
        TextFlow flow = new TextFlow(
                new Text("Verzija: 1.0.0-SNAPSHOT\nGithub repozitorij: "),
                hyperlink,
                new Text("\nAutor: Edis Krsmanović\n"),
                new ImageView("/img/icon.png")
        );
        alert.getDialogPane().setContent(flow);
        alert.showAndWait();
    }

    public void saveAction(ActionEvent actionEvent) {

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        File file = fileChooser.showSaveDialog(new Stage());

        if (file != null) {
            model.zapisiDatoteku(file);
        }
    }

    public void printAction(ActionEvent actionEvent) {
        try {
            new PrintReport().showReport(model.getConn());
        } catch (JRException e1) {
            e1.printStackTrace();
        }
    }

    public void bosanskiAction(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("bs", "BA"));
        changeLanguage();
    }

    public void englishAction(ActionEvent actionEvent) {
        Locale.setDefault(new Locale("en", "US"));
        changeLanguage();
    }

    private void changeLanguage() {
        ResourceBundle resourceBundle = ResourceBundle.getBundle("language", Locale.getDefault());

        btnObrisi.setText(resourceBundle.getString("remove"));
        btnDodaj.setText(resourceBundle.getString("add"));
        btnKraj.setText(resourceBundle.getString("exit"));

        menuFile.setText(resourceBundle.getString("menu.file"));
        menuHelp.setText(resourceBundle.getString("menu.help"));

        fileSave.setText(resourceBundle.getString("file.save"));
        filePrint.setText(resourceBundle.getString("file.print"));
        fileLanguage.setText(resourceBundle.getString("file.language"));
        fileExit.setText(resourceBundle.getString("file.exit"));
        helpAbout.setText(resourceBundle.getString("help.about"));

        labelName.setText(resourceBundle.getString("name"));
        labelSurname.setText(resourceBundle.getString("surname"));
        labelEmail.setText(resourceBundle.getString("email"));
        labelUsername.setText(resourceBundle.getString("username"));
        labelPassword.setText(resourceBundle.getString("password"));
    }
}
