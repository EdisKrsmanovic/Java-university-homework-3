package ba.unsa.etf.rpr.t7;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class KorisniciModel {
    private ObservableList<Korisnik> korisnici = FXCollections.observableArrayList();
    private SimpleObjectProperty<Korisnik> trenutniKorisnik = new SimpleObjectProperty<>();
    private Connection konekcija;

    public KorisniciModel() {
        if (!Files.exists(Paths.get("korisnici.db"))) {
            regenerisiBazu();
        } else {
            try {
                konekcija = DriverManager.getConnection("jdbc:sqlite:korisnici.db");
            } catch (SQLException e) {
                System.out.println(String.format("Greska pri spajanju s bazom: %s", e.getMessage()));
            }
        }
    }

    public void napuni() {
        korisnici.addAll(uzmiKorisnike());
        trenutniKorisnik.set(null);
    }

    public void diskonektuj() {
        try {
            konekcija.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
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
        if (this.trenutniKorisnik.getValue() != null) updateuj(this.trenutniKorisnik.getValue());
        this.trenutniKorisnik.set(trenutniKorisnik);
    }

    public void setTrenutniKorisnik(int i) {
        this.trenutniKorisnik.set(korisnici.get(i));
    }

    public void obrisiTrenutnog() {
        obrisi(trenutniKorisnik.getValue());
        korisnici.remove(trenutniKorisnik.getValue());
    }

    public void zapisiDatoteku(File file) {
        try {
            if (file != null) {
                FileWriter fileWriter = new FileWriter(file);
                String stringKorisnici = uzmiKorisnike().stream().map(Korisnik::passwdFormat).collect(Collectors.joining("\n"));
                fileWriter.write(stringKorisnici);
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<Korisnik> uzmiKorisnike() {
        List<Korisnik> noviKorisnici = new ArrayList<>();

        try {
            PreparedStatement stmt = konekcija.prepareStatement("SELECT * FROM korisnik");


            ResultSet rs = null;
            rs = stmt.executeQuery();

            while (rs.next()) {
                Korisnik korisnik = new Korisnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6), rs.getString(7));
                noviKorisnici.add(korisnik);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return noviKorisnici;
    }

    private void updateuj(Korisnik korisnik) {
        if(korisnik.getId() == null) {
            try {
                PreparedStatement stmt = konekcija.prepareStatement("INSERT INTO korisnik VALUES(null,?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
                stmt.setString(1, korisnik.getIme());
                stmt.setString(2, korisnik.getPrezime());
                stmt.setString(3, korisnik.getEmail());
                stmt.setString(4, korisnik.getUsername());
                stmt.setString(5, korisnik.getPassword());
                stmt.setString(6, korisnik.getSlika());
                stmt.execute();
                ResultSet rs = stmt.getGeneratedKeys();
                if (rs.next()) {
                    korisnik.setId(rs.getInt(1));
                }
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                PreparedStatement stmt = konekcija.prepareStatement("UPDATE korisnik SET ime = ?, prezime = ?, email = ?, username = ?, password = ?, slika = ? WHERE id = ?");
                stmt.setString(1, korisnik.getIme());
                stmt.setString(2, korisnik.getPrezime());
                stmt.setString(3, korisnik.getEmail());
                stmt.setString(4, korisnik.getUsername());
                stmt.setString(5, korisnik.getPassword());
                stmt.setString(6, korisnik.getSlika());
                stmt.setInt(7, korisnik.getId());
                stmt.execute();
                stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private void obrisi(Korisnik korisnik) {
        try {
            PreparedStatement stmt = konekcija.prepareStatement("DELETE FROM korisnik WHERE id = ?");
            stmt.setInt(1, korisnik.getId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void regenerisiBazu() {
        try {
            konekcija = DriverManager.getConnection("jdbc:sqlite:korisnici.db");
            Scanner ulaz = new Scanner(new FileInputStream("korisnici.db.sql"));
            StringBuilder upit = new StringBuilder();
            while (ulaz.hasNext()) {
                upit.append(ulaz.nextLine());
                if (upit.length() > 1) {
                    if (upit.charAt(upit.length() - 1) == ';') {
                        PreparedStatement stmt = konekcija.prepareStatement(upit.toString());
                        stmt.execute();
                        upit = new StringBuilder();
                    }
                }
            }
        } catch (FileNotFoundException | SQLException e) {
            System.out.println(String.format("Greska pri generisanju baze: %s", e.getMessage()));
        }
    }

    public Connection getConn() {
        return konekcija;
    }
}
