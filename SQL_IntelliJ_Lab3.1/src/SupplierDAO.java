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
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, supplier.getId());
            pstmt.setString(2, supplier.getCompanyName());
            pstmt.setString(3, supplier.getDirectorName());
            pstmt.setString(4, supplier.getDirectorPhone());
            pstmt.setString(5, supplier.getDirectorEmail());
            pstmt.executeUpdate();
        }
    }

    public Supplier getSupplier(int id) throws SQLException {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Supplier(
                        rs.getInt("id"),
                        rs.getString("company_name"),
                        rs.getString("director_name"),
                        rs.getString("director_phone"),
                        rs.getString("director_email")
                );
            }
        }
        return null;
    }

    public List<Supplier> getAllSuppliers() throws SQLException {
        List<Supplier> suppliers = new ArrayList<>();
        String sql = "SELECT * FROM suppliers ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                suppliers.add(new Supplier(
                        rs.getInt("id"),
                        rs.getString("company_name"),
                        rs.getString("director_name"),
                        rs.getString("director_phone"),
                        rs.getString("director_email")
                ));
            }
        }
        return suppliers;
    }
}