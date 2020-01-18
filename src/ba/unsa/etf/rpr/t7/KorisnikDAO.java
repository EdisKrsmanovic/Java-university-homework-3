package ba.unsa.etf.rpr.t7;

import javafx.beans.property.SimpleObjectProperty;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.*;

public class KorisnikDAO {
    private static KorisnikDAO instance = null;

    private static Connection konekcija = null;
    private Map<String, Korisnik> korisnici = new LinkedHashMap<>();

    public static KorisnikDAO getInstance() {
        if (instance == null) instance = new KorisnikDAO();
        return instance;
    }

    public static void removeInstance() {
        if (instance != null) {
            try {
                konekcija.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        instance = null;
    }

    private KorisnikDAO() {
        try {

            File dbfile = new File("korisnici.db");
            dbfile.delete();
            generisiBazu();
            konekcija = DriverManager.getConnection("jdbc:sqlite:korisnici.db");

            PreparedStatement stmt = konekcija.prepareStatement("SELECT * FROM korisnik");
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Korisnik korisnik = new Korisnik(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                korisnici.put(korisnik.getUsername(), korisnik);
            }
        } catch (SQLException e) {
            System.out.println(String.format("Error pri spajanju sa bazom: %s", e.getMessage()));
        }
    }


    public Collection<Korisnik> getKorisnici() {
        return korisnici.values();
    }


    public static void generisiBazu() {
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

    public void updateuj(Korisnik trenutniKorisnik) {
        try {
            PreparedStatement stmt = konekcija.prepareStatement("UPDATE korisnik SET ime = ?, prezime = ?, email = ?, username = ?, password = ? WHERE id = ?");

            stmt.setString(1, trenutniKorisnik.getIme());
            stmt.setString(2, trenutniKorisnik.getPrezime());
            stmt.setString(3, trenutniKorisnik.getEmail());
            stmt.setString(4, trenutniKorisnik.getUsername());
            stmt.setString(5, trenutniKorisnik.getPassword());
            stmt.setInt(6, trenutniKorisnik.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public void obrisi(Korisnik korisnik) {
        try {
            PreparedStatement stmt = konekcija.prepareStatement("DELETE FROM korisnik WHERE id = ?");
            stmt.setInt(1, korisnik.getId());
            stmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}
