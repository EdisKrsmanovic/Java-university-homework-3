create table korisnik(
    ID INTEGER PRIMARY KEY AUTOINCREMENT,
    ime varchar(50),
    prezime varchar(50),
    email varchar(50),
    username varchar(50),
    password varchar(50)
);

insert into korisnik values(null, 'Vedran', 'Ljubović', 'vljubovic@etf.unsa.ba', 'vedranlj', 'test');
insert into korisnik values(null, 'Amra', 'Delić', 'adelic@etf.unsa.ba', 'amrad', 'test');
insert into korisnik values(null, 'Tarik', 'Sijerčić', 'tsijercic1@etf.unsa.ba', 'tariks', 'test');
insert into korisnik values(null, 'Rijad', 'Fejzić', 'rfejzic1@etf.unsa.ba', 'rijadf', 'test');
