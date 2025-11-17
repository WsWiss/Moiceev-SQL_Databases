import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SupplierDAO {
    private Connection conn;

    public SupplierDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Supplier supplier) throws SQLException {
        String sql = "INSERT INTO suppliers (id, company_name, director_name, director_phone, director_email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, supplier.getId());
            preparedStatement.setString(2, supplier.getCompanyName());
            preparedStatement.setString(3, supplier.getDirectorName());
            preparedStatement.setString(4, supplier.getDirectorPhone());
            preparedStatement.setString(5, supplier.getDirectorEmail());
            preparedStatement.executeUpdate();
        }
    }

    public Supplier getSupplier(int id) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return new Supplier(
                        resultSet.getInt("id"),
                        resultSet.getString("company_name"),
                        resultSet.getString("director_name"),
                        resultSet.getString("director_phone"),
                        resultSet.getString("director_email")
                );
            }
        }
        return null;
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY id";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                suppliers.add(new Supplier(
                        resultSet.getInt("id"),
                        resultSet.getString("company_name"),
                        resultSet.getString("director_name"),
                        resultSet.getString("director_phone"),
                        resultSet.getString("director_email")
                ));
            }
        }
        return suppliers;
    }
}