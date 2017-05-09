DROP TABLE IF EXISTS Atom;
CREATE TABLE Atom(
  Z INTEGER NOT NULL,
  Name VARCHAR NOT NULL,
  Symbol VARCHAR NOT NULL,
  Radius INTEGER NOT NULL,
  Color VARCHAR NOT NULL,
  "Mass number" INTEGER NOT NULL,
  Comments VARCHAR,
  PRIMARY KEY (Z)
);

DROP TABLE IF EXISTS valence;
CREATE TABLE valence(
  valenceId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  elementId INTEGER NOT NULL,
  valence TINYINT(1),
  FOREIGN KEY (elementId) REFERENCES Atom(Z)
);

DROP TABLE IF EXISTS Combination;
CREATE TABLE Combination(
  combId INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  elementOneId INTEGER NOT NULL,
  elementTwoId INTEGER NOT NULL,
  FOREIGN KEY (elementOneId) REFERENCES Atom(Z),
  FOREIGN KEY (elementTwoId) REFERENCES Atom(Z)
);

DROP TABLE IF EXISTS Molecule;
CREATE TABLE Molecule(
  CompoundID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
  Compound VARCHAR NOT NULL,
  Comment VARCHAR NOT NULL,
  Phase FLOAT NOT NULL,
  MeltingPoint FLOAT,
  BoilingPoint FLOAT,
  Density FLOAT
);