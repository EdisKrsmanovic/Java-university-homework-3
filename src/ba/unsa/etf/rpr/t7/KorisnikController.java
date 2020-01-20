package ba.unsa.etf.rpr.t7;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import net.sf.jasperreports.engine.JRException;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static javafx.scene.layout.Region.USE_COMPUTED_SIZE;

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
    public Button imgKorisnik;

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

        ImageView imageView = new ImageView("/img/face-smile.png");
        imageView.setFitWidth(128);
        imageView.setFitHeight(128);
        imgKorisnik.setGraphic(imageView);

        model.trenutniKorisnikProperty().addListener((obs, oldKorisnik, newKorisnik) -> {
            if (oldKorisnik != null) {
                fldIme.textProperty().unbindBidirectional(oldKorisnik.imeProperty());
                fldPrezime.textProperty().unbindBidirectional(oldKorisnik.prezimeProperty());
                fldEmail.textProperty().unbindBidirectional(oldKorisnik.emailProperty());
                fldUsername.textProperty().unbindBidirectional(oldKorisnik.usernameProperty());
                fldPassword.textProperty().unbindBidirectional(oldKorisnik.passwordProperty());

                if (newKorisnik.getSlika() != null) {
                    ImageView img = new ImageView(newKorisnik.getSlika());
                    img.setFitWidth(128);
                    img.setFitHeight(128);
                    imgKorisnik.setGraphic(img);
                }
            }
            if (newKorisnik == null) {
                fldIme.setText("");
                fldPrezime.setText("");
                fldEmail.setText("");
                fldUsername.setText("");
                fldPassword.setText("");

                ImageView img = new ImageView("/img/face-smile.png");
                img.setFitWidth(128);
                img.setFitHeight(128);
                imgKorisnik.setGraphic(img);
            } else {
                fldIme.textProperty().bindBidirectional(newKorisnik.imeProperty());
                fldPrezime.textProperty().bindBidirectional(newKorisnik.prezimeProperty());
                fldEmail.textProperty().bindBidirectional(newKorisnik.emailProperty());
                fldUsername.textProperty().bindBidirectional(newKorisnik.usernameProperty());
                fldPassword.textProperty().bindBidirectional(newKorisnik.passwordProperty());

                if(newKorisnik.getSlika() != null) {
                    ImageView img = new ImageView(newKorisnik.getSlika());
                    img.setFitWidth(128);
                    img.setFitHeight(128);
                    imgKorisnik.setGraphic(img);
                }
                else {
                    ImageView img = new ImageView("/img/face-smile.png");
                    img.setFitWidth(128);
                    img.setFitHeight(128);
                    imgKorisnik.setGraphic(img);
                }
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

    public void imageAction(ActionEvent actionEvent) {
        if (model.getTrenutniKorisnik() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nijedan korisnik nije izabran");
            alert.setHeaderText("Niste izabrali korisnika kojeg želite");
            alert.setContentText("Dodajte novog korisnika ili odaberite vec nekog postojeceg");
            alert.showAndWait();
        } else {
            try {
                Stage stage = new Stage();
                PretragaController ctrl = new PretragaController();
                ctrl.setKorisnik(model.getTrenutniKorisnik());
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/pretraga.fxml"));
                loader.setController(ctrl);
                Parent root = loader.load();
                stage.setTitle("Pretraga slike");
                stage.setScene(new Scene(root, USE_COMPUTED_SIZE, USE_COMPUTED_SIZE));
                stage.show();

                stage.setOnHidden(windowEvent -> {
                    if (model.getTrenutniKorisnik() != null) {
                        ImageView img = new ImageView(model.getTrenutniKorisnik().getSlika());
                        img.setFitWidth(128);
                        img.setFitHeight(128);
                        imgKorisnik.setGraphic(img);
                    }
                });
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
