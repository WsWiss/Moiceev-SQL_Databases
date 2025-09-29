import java.sql.*;
import java.nio.file.*;
import java.io.IOException;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:h2:./Database/products;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            //createTable(conn);
            //insertDataFromFile(conn);

            // Вывод данных для проверки
            System.out.println("База данных успешно создана и заполнена!");
            System.out.println("\nСодержимое таблицы products:");
            displayData(conn);

        } catch (SQLException e) {

            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTable(Connection conn) {
        String sql = """
            CREATE TABLE IF NOT EXISTS products (
                id INT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                category VARCHAR(100),
                cost DECIMAL(10,2),
                amount INT
            )
            """;
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
        catch (SQLException e)
        {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void insertDataFromFile(Connection conn) throws SQLException {
        String sql = "INSERT INTO products (id, name, category, cost, amount) VALUES (?, ?, ?, ?, ?)";

        try {
            Path path = Paths.get("data.txt");
            String content = Files.readString(path);
            String[] lines = content.split("\n");

            try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
                for (String line : lines) {
                    String[] parts = line.split(", ");
                    if (parts.length == 5) {
                        pstmt.setInt(1, Integer.parseInt(parts[0].trim()));
                        pstmt.setString(2, parts[1].trim());
                        pstmt.setString(3, parts[2].trim());
                        pstmt.setDouble(4, Double.parseDouble(parts[3].trim()));
                        pstmt.setInt(5, Integer.parseInt(parts[4].trim()));
                        pstmt.addBatch();
                    }
                }
                pstmt.executeBatch();
            }
        } catch (IOException e) {
            System.err.println("Ошибка чтения файла data.txt: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void displayData(Connection conn) throws SQLException {
        String sql = "SELECT * FROM products WHERE category = 'Ноутбук' ORDER BY id";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("┌────┬────────────────────────────────┬──────────────────────┬──────────┬────────┐");
            System.out.println("│ ID │ Наименование                   │ Категория            │   Цена   │ Кол-во │");
            System.out.println("├────┼────────────────────────────────┼──────────────────────┼──────────┼────────┤");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String category = rs.getString("category");
                double cost = rs.getDouble("cost");
                int amount = rs.getInt("amount");

                // Обрезаем длинные названия
                if (name.length() > 30) {
                    name = name.substring(0, 27) + "...";
                }
                if (category.length() > 20) {
                    category = category.substring(0, 17) + "...";
                }

                System.out.printf("│ %-2d │ %-30s │ %-20s │ %8.2f │ %6d │%n",
                        id, name, category, cost, amount);
            }
            System.out.println("└────┴────────────────────────────────┴──────────────────────┴──────────┴────────┘");

            // Вывод статистики
            displayStatistics(conn);
        }
    }

    private static void displayStatistics(Connection conn) throws SQLException {
        String sql = "SELECT COUNT(*) as total_products, SUM(cost * amount) as total_value FROM products WHERE category = 'Ноутбук'";
        try (Statement statement = conn.createStatement();
             ResultSet rs = statement.executeQuery(sql)) {

            if (rs.next()) {
                int totalProducts = rs.getInt("total_products");
                double totalValue = rs.getDouble("total_value");
                System.out.printf("\nСтатистика:");
                System.out.printf("\nВсего товаров: %d", totalProducts);
                System.out.printf("\nОбщая стоимость inventory: %.2f руб.\n", totalValue);
            }
        }
    }
}