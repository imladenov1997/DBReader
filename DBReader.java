import java.awt.*;
import java.sql.*;
import java.util.Random;


class DBReader {
    // Statement from connection
    private Statement statement;

    // ResultSet of a Query
    private ResultSet rs;

    DBReader() {
        this.initialiseConnection();
    }

    private void initialiseConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:testdb.db");
            this.statement = connection.createStatement();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Get the Z-numbers of the atoms that can create a binding with the specified atom
    private Integer[] getCombination(int z) {
        String combinationQuery = "SELECT Z FROM Atom" +
                "                  JOIN Combination ON" +
                "                  Atom.Z = elementTwoId AND elementOneId = " + z +
                "                  OR Atom.Z = elementOneId AND elementTwoId = " + z + ";";
        int count = 0; // counter in the index
        try {
        rs = statement.executeQuery(combinationQuery);
        Integer[] elements = new Integer[this.getNumberOfElements(rs, combinationQuery)];
            while(rs.next()) {
                elements[count++] = rs.getInt("Z");
            }
            return elements;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Select the row of the specified atom and extract all the data
    private Atom createAtom(int z) {
        String createAtomQuery = "SELECT * FROM Atom" +
                "                 JOIN valence" +
                "                 WHERE " + z + " = Atom.Z AND " + z + " = valence.elementId";
        Random random = new Random(); // for random position on the display
        String name;
        int number;
        int radius;
        int valence;
        Color color;
        try {
            rs = statement.executeQuery(createAtomQuery);
            while (rs.next()) {
                if (rs.getInt("Z") == z) {
                    name = rs.getString("Symbol");
                    number = rs.getInt("Z");
                    radius = rs.getInt("Radius");
                    valence = rs.getInt("Valence");
                    color = Color.decode(rs.getString("Color"));
                    return new Atom(name, number, radius, valence, color, random.nextInt(500), random.nextInt(500), getCombination(z));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Get List of Atoms (only the names)
    String[] getNextElement() {
        String listAtomsQuery = "SELECT Name FROM Atom;";
        String[] atomList;
        int counter = 0;

        // Add each atom in the list
        try {
            rs = statement.executeQuery(listAtomsQuery);
            atomList = new String[getNumberOfElements(rs, listAtomsQuery)];
            while(rs.next()) {
                atomList[counter] = rs.getString("Name");
                counter++;
            }
            return atomList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Choose an element from the database by its name
    Atom selectElement(String name) {
        String selectElementQuery = "SELECT Z FROM Atom " +
                "                    JOIN valence" +
                "                    WHERE Name = '" + name + "';";

        try {
            rs = statement.executeQuery(selectElementQuery);
            rs.next();
            return this.createAtom(rs.getInt("Z"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // Return the number of all elements from a certain query (number of rows)
    private int getNumberOfElements(ResultSet rs, String query) throws SQLException {
        int num = 0;
        while (rs.next()) {
            num++;
        }
        rs = statement.executeQuery(query);
        return num;
    }

    // Check whether there is an existing molecule
    boolean validateMolecule(String molecule) {
        String query = "SELECT * FROM Molecule" +
                "       WHERE Compound = '" + molecule + "'";
        try {
            rs = statement.executeQuery(query);
            if (rs.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Return the description of a molecule
    String getMoleculeDescription(String compound) {
        if (validateMolecule(compound)) {
            try {
                return rs.getString("Comment");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return "No Description";
    }
}
