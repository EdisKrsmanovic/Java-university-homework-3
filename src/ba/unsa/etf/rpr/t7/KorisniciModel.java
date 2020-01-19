package ba.unsa.etf.rpr.t7;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.lang.ref.Cleaner;
import java.nio.file.Files;
import java.nio.file.Paths;

public class KorisniciModel {
    private ObservableList<Korisnik> korisnici = FXCollections.observableArrayList();
    private SimpleObjectProperty<Korisnik> trenutniKorisnik = new SimpleObjectProperty<>();
    private KorisnikDAO konekcija;

    public KorisniciModel() {
        if(!Files.exists(Paths.get("korisnici.db"))) KorisnikDAO.removeInstance();
        konekcija = KorisnikDAO.getInstance();
        konekcija.refreshKorisnici();
    }

    public void napuni() {
        korisnici.addAll(konekcija.getKorisnici());
        trenutniKorisnik.set(null);
    }

    public void diskonektuj() {
        KorisnikDAO.removeInstance();
    }

    public ObservableList<Korisnik> getKorisnici() {
        return korisnici;
    }

    public void setKorisnici(ObservableList<Korisnik> korisnici) {
        this.korisnici = korisnici;
    }

    public Korisnik getTrenutniKorisnik() {
        return trenutniKorisnik.get();
    }

    public SimpleObjectProperty<Korisnik> trenutniKorisnikProperty() {
        return trenutniKorisnik;
    }

    public void setTrenutniKorisnik(Korisnik trenutniKorisnik) {
        if(this.trenutniKorisnik.getValue() != null) konekcija.updateuj(this.trenutniKorisnik.getValue());
        this.trenutniKorisnik.set(trenutniKorisnik);
    }

    public void setTrenutniKorisnik(int i) {
        this.trenutniKorisnik.set(korisnici.get(i));
    }

    public void obrisiTrenutnog() {
        konekcija.obrisi(trenutniKorisnik.getValue());
        korisnici.remove(trenutniKorisnik.getValue());
    }
}
