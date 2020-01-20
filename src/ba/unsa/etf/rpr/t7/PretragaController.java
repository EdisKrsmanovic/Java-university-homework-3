package ba.unsa.etf.rpr.t7;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
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
//        Text value = new Text("blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla blablabla");
//        value.setWrappingWidth(500);
//        scrImgPane.setContent(value);
//        scrImgPane.setFitToWidth(true);
    }

    public void searchAction() {
        //threading, na svaku novu sliku staviti listener i ako se klikne staviti imageurl na nju, te skinuti sliku u img folder?
        pretrazi(textSearch.getText());
    }

    private void pretrazi(String text) {
        try {
            URL giphy = new URL(String.format("https://api.giphy.com/v1/gifs/search?api_key=oNpc1jLCGwTMlUOrBdl9BdSD439AbTXl&q=%s&limit=25&offset=0&rating=G&lang=en", text));
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
            flow.setPrefWrapLength(350);

            ImageView pages[] = new ImageView[items.length()];
            for (int i = 0; i < items.length(); i++) {

                int finalI = i;
                Thread thread = new Thread(new Runnable() {
                    private int id = finalI;


                    @Override
                    public void run() {
                        JSONObject slike = items.getJSONObject(id);
                        String jsonSlika = slike.getJSONObject("images").get("480w_still").toString();
                        JSONObject slika = new JSONObject(jsonSlika);
//                System.out.println(slika.get("url").toString().replace("?", "\n").split("\n")[0].replaceAll("media[0-9]", "i"));
                        ImageView imageView = new ImageView(slika.get("url").toString().replace("?", "\n").split("\n")[0].replaceAll("media[0-9]", "i"));
                        imageView.setFitWidth(128);
                        imageView.setFitHeight(128);
                        imageView.setOnMouseClicked(e -> {
                            ImageView selectedImage = (ImageView) e.getSource();
                            imageUrl = selectedImage.getImage().getUrl();
                        });
                        pages[id] = imageView;
                        flow.getChildren().add(pages[id]);
                        scrImgPane.setContent(flow);
                    }
                });

                thread.run();


            }
            scrImgPane.setFitToWidth(true);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void okAction() {
        if (imageUrl == null || imageUrl.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Nijedna slika nije izabrana");
            alert.setHeaderText("Niste izabrali sliku koju želite");
            alert.setContentText("Unesite pretragu a zatim izaberite sliku, ili kliknite na Cancel ako ne zelite novu sliku");
            alert.showAndWait();
        } else {
            korisnik.setSlika(imageUrl);
            Stage stage = (Stage) textSearch.getScene().getWindow();
            stage.close();
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
