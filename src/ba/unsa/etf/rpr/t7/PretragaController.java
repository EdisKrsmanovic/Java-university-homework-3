package ba.unsa.etf.rpr.t7;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class PretragaController {
    public Korisnik korisnik;

    public ScrollPane scrImgPane;
    public TextField textSearch;

    private String imageUrl = null;

    @FXML
    public void initialize() {
        scrImgPane.setFitToHeight(true);
        scrImgPane.setFitToWidth(true);
    }

    public void searchAction() {
        pretrazi(textSearch.getText());
    }

    private void pretrazi(String text) {
        new Thread(() -> {
            try {
                URL giphy = new URL(String.format("https://api.giphy.com/v1/gifs/search?api_key=oNpc1jLCGwTMlUOrBdl9BdSD439AbTXl&q=%s&limit=25&offset=0&rating=R&lang=en", text));
                URLConnection yc = giphy.openConnection();
                BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                                yc.getInputStream()));
                String inputLine;

                StringBuilder json = new StringBuilder();
                while ((inputLine = in.readLine()) != null) {
                    json.append(inputLine);
                }
                in.close();


                JSONObject obj = new JSONObject(json.toString());

                JSONArray items = obj.getJSONArray("data");

                FlowPane flow = new FlowPane();
                flow.setPadding(new Insets(5, 0, 5, 0));
                flow.setVgap(4);
                flow.setHgap(4);

                for (int i = 0; i < items.length(); i++) {
                    ImageView imageView = new ImageView("/img/loading.gif");
                    imageView.setFitWidth(128);
                    imageView.setFitHeight(128);

                    Button button = new Button();
                    button.setGraphic(imageView);
                    button.setOnMouseClicked(e -> {
                        Button selectedImage = (Button) e.getSource();
                        ImageView img = (ImageView) selectedImage.getGraphic();
                        imageUrl = img.getImage().getUrl();
                    });
                    Platform.runLater(() -> {
                        flow.getChildren().add(button);
                        scrImgPane.setContent(flow);
                    });

                    JSONObject slike = items.getJSONObject(i);
                    String jsonSlika = slike.getJSONObject("images").get("original_still").toString();
                    JSONObject slika = new JSONObject(jsonSlika);

                    imageView.setImage(new Image(slika.get("url").toString().replace("?", "\n").split("\n")[0].replaceAll("media[0-9]", "i")));
                    imageView.setFitWidth(128);
                    imageView.setFitHeight(128);
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void okAction() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nijedna slika nije izabrana");
            alert.setHeaderText("Niste izabrali sliku koju želite");
            alert.setContentText("Unesite pretragu a zatim izaberite sliku, ili kliknite na Cancel ako ne zelite novu sliku");
            alert.showAndWait();
        } else {
            if(korisnik != null) korisnik.setSlika(imageUrl);
            Stage stage = (Stage) textSearch.getScene().getWindow();
            stage.close();
        }
    }

    public void cancelAction() {
        Stage stage = (Stage) textSearch.getScene().getWindow();
        stage.close();
    }

    public void setKorisnik(Korisnik korisnik) {
        this.korisnik = korisnik;
    }
}
