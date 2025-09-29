// ProductDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
    private Connection conn;

    public ProductDAO(Connection conn) {
        this.conn = conn;
    }

    public void insert(Product product) throws SQLException {
        String sql = "INSERT INTO products (id, name, supplier, category, cost, amount) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, product.getId());
            pstmt.setString(2, product.getName());
            pstmt.setString(3, product.getSupplier());
            pstmt.setString(4, product.getCategory());
            pstmt.setDouble(5, product.getCost());
            pstmt.setInt(6, product.getAmount());
            pstmt.executeUpdate();
        }
    }

    public Product getProduct(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("supplier"),
                        rs.getString("category"),
                        rs.getDouble("cost"),
                        rs.getInt("amount")
                );
            }
        }
        return null;
    }

    public List<Product> getProductsByCategory(String category) throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products WHERE category = ? ORDER BY id";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, category);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("supplier"),
                        rs.getString("category"),
                        rs.getDouble("cost"),
                        rs.getInt("amount")
                ));
            }
        }
        return products;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                products.add(new Product(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("supplier"),
                        rs.getString("category"),
                        rs.getDouble("cost"),
                        rs.getInt("amount")
                ));
            }
        }
        return products;
    }
}