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
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, product.getId());
            preparedStatement.setString(2, product.getName());
            preparedStatement.setString(3, product.getSupplier());
            preparedStatement.setString(4, product.getCategory());
            preparedStatement.setDouble(5, product.getCost());
            preparedStatement.setInt(6, product.getAmount());
            preparedStatement.executeUpdate();

    }

    public Product getProduct(int id) throws SQLException {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);
            ResultSet rs = preparedStatement.executeQuery();
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
        try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
            preparedStatement.setString(1, category);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("supplier"),
                        resultSet.getString("category"),
                        resultSet.getDouble("cost"),
                        resultSet.getInt("amount")
                ));
            }
        }
        return products;
    }

    public List<Product> getAllProducts() throws SQLException {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM products ORDER BY id";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                products.add(new Product(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        resultSet.getString("supplier"),
                        resultSet.getString("category"),
                        resultSet.getDouble("cost"),
                        resultSet.getInt("amount")
                ));
            }
        }
        return products;
    }
}