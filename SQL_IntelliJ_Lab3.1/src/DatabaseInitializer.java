import java.sql.*;
import java.nio.file.*;
import java.io.IOException;
import java.util.*;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:h2:./Database/products;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    public static void main(String[] args) {
        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            createTables(conn);

            // Initialize DAOs
            SupplierDAO supplierDAO = new SupplierDAO(conn);
            ProductDAO productDAO = new ProductDAO(conn);

            // Create and insert suppliers from file
            List<Supplier> suppliers = createSuppliers();
            for (Supplier supplier : suppliers) {
                supplierDAO.insert(supplier);
            }

            // Create and insert products from file
            List<Product> products = createProducts();
            for (Product product : products) {
                productDAO.insert(product);
            }

            System.out.println("База данных успешно создана и заполнена!");

            // Display all suppliers
            System.out.println("\n=== ВСЕ ПОСТАВЩИКИ ===");
            displaySuppliers(supplierDAO);

            // Display all products
            System.out.println("\n=== ВСЕ ТОВАРЫ ===");
            displayProducts(productDAO);

            displaySupplierContacts(supplierDAO, productDAO);

        } catch (SQLException | IOException e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void createTables(Connection conn) throws SQLException {
        // Create suppliers table (без категорий)
        String createSuppliersTable = """
            CREATE TABLE IF NOT EXISTS suppliers (
                id INT PRIMARY KEY,
                company_name VARCHAR(255) NOT NULL,
                director_name VARCHAR(255),
                director_phone VARCHAR(20),
                director_email VARCHAR(255)
            )
            """;

        // Create products table с supplier
        String createProductsTable = """
            CREATE TABLE IF NOT EXISTS products (
                id INT PRIMARY KEY,
                name VARCHAR(255) NOT NULL,
                supplier VARCHAR(255),
                category VARCHAR(100),
                cost DECIMAL(10,2),
                amount INT
            )
            """;

        try (Statement stmt = conn.createStatement()) {
            stmt.execute(createSuppliersTable);
            stmt.execute(createProductsTable);
        }
    }

    private static List<Supplier> createSuppliers() throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Path path = Paths.get("suppliers.txt");
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            String[] parts = line.split(",");
            if (parts.length == 5) {
                int id = Integer.parseInt(parts[0].trim());
                String companyName = parts[1].trim();
                String directorName = parts[2].trim();
                String directorPhone = parts[3].trim();
                String directorEmail = parts[4].trim();

                suppliers.add(new Supplier(id, companyName, directorName, directorPhone, directorEmail));
            }
        }
        return suppliers;
    }

    private static List<Product> createProducts() throws IOException {
        List<Product> products = new ArrayList<>();
        Path path = Paths.get("data.txt");
        List<String> lines = Files.readAllLines(path);

        for (String line : lines) {
            String[] parts = line.split(", ");
            if (parts.length == 6) {
                int id = Integer.parseInt(parts[0].trim());
                String name = parts[1].trim();
                String supplier = parts[2].trim();
                String category = parts[3].trim();
                double cost = Double.parseDouble(parts[4].trim());
                int amount = Integer.parseInt(parts[5].trim());

                products.add(new Product(id, name, supplier, category, cost, amount));
            }
        }
        return products;
    }

    private static void displayProducts(ProductDAO productDAO) throws SQLException {
        List<Product> products = productDAO.getAllProducts();

        System.out.println("\n┌────┬────────────────────────────────┬──────────────────────┬──────────────────────┬──────────┬────────┐");
        System.out.println("│ ID │ Наименование                   │ Поставщик            │ Категория            │   Цена   │ Кол-во │");
        System.out.println("├────┼────────────────────────────────┼──────────────────────┼──────────────────────┼──────────┼────────┤");

        for (Product product : products) {
            String name = product.getName();
            String supplier = product.getSupplier();
            String category = product.getCategory();

            if (name.length() > 30) {
                name = name.substring(0, 27) + "...";
            }
            if (supplier.length() > 20) {
                supplier = supplier.substring(0, 17) + "...";
            }
            if (category.length() > 20) {
                category = category.substring(0, 17) + "...";
            }

            System.out.printf("│ %-2d │ %-30s │ %-20s │ %-20s │ %8.2f │ %6d │%n",
                    product.getId(), name, supplier, category, product.getCost(), product.getAmount());
        }
        System.out.println("└────┴────────────────────────────────┴──────────────────────┴──────────────────────┴──────────┴────────┘");

        displayAllProductsStatistics(products);
    }

    private static void displayAllProductsStatistics(List<Product> products) {
        int totalProducts = products.size();
        double totalValue = products.stream().mapToDouble(p -> p.getCost() * p.getAmount()).sum();
        long uniqueCategories = products.stream().map(Product::getCategory).distinct().count();
        long uniqueSuppliers = products.stream().map(Product::getSupplier).distinct().count();

        System.out.printf("\nОбщая статистика по товарам:");
        System.out.printf("\nВсего товаров: %d", totalProducts);
        System.out.printf("\nУникальных категорий: %d", uniqueCategories);
        System.out.printf("\nУникальных поставщиков: %d", uniqueSuppliers);
        System.out.printf("\nОбщая стоимость inventory: %.2f руб.\n", totalValue);
    }

    private static void displaySuppliers(SupplierDAO supplierDAO) throws SQLException {
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();

        System.out.println("\n┌────┬──────────────────────┬──────────────────────┬──────────────────┬──────────────────────────┐");
        System.out.println("│ ID │ Компания             │ Директор             │ Телефон          │ Email                    │");
        System.out.println("├────┼──────────────────────┼──────────────────────┼──────────────────┼──────────────────────────┤");

        for (Supplier supplier : suppliers) {
            String companyName = supplier.getCompanyName();
            String directorName = supplier.getDirectorName();
            String directorPhone = supplier.getDirectorPhone();
            String directorEmail = supplier.getDirectorEmail();

            if (companyName.length() > 20) {
                companyName = companyName.substring(0, 17) + "...";
            }
            if (directorName.length() > 20) {
                directorName = directorName.substring(0, 17) + "...";
            }
            if (directorPhone.length() > 16) {
                directorPhone = directorPhone.substring(0, 13) + "...";
            }
            if (directorEmail.length() > 24) {
                directorEmail = directorEmail.substring(0, 21) + "...";
            }

            System.out.printf("│ %-2d │ %-20s │ %-20s │ %-16s │ %-24s │%n",
                    supplier.getId(), companyName, directorName, directorPhone, directorEmail);
        }
        System.out.println("└────┴──────────────────────┴──────────────────────┴──────────────────┴──────────────────────────┘");

        displaySuppliersStatistics(suppliers);
    }

    private static void displaySuppliersStatistics(List<Supplier> suppliers) {
        int totalSuppliers = suppliers.size();
        System.out.printf("\nСтатистика по поставщикам:");
        System.out.printf("\nВсего поставщиков: %d\n", totalSuppliers);
    }

    private static void displaySupplierContacts(SupplierDAO supplierDAO, ProductDAO productDAO) throws SQLException {
        // Получаем данные из обеих таблиц
        List<Supplier> suppliers = supplierDAO.getAllSuppliers();
        List<Product> products = productDAO.getAllProducts();

        // Создаем мапу для подсчета количества товаров по поставщикам
        Map<String, Integer> productCountBySupplier = new HashMap<>();
        for (Product product : products) {
            String supplierName = product.getSupplier();
            productCountBySupplier.put(supplierName,
                    productCountBySupplier.getOrDefault(supplierName, 0) + 1);
        }

        System.out.println("\n┌──────────────────────┬──────────────────────┬──────────────────┬──────────┐");
        System.out.println("│ Наименование         │ Директор             │ Телефон          │ Товаров  │");
        System.out.println("├──────────────────────┼──────────────────────┼──────────────────┼──────────┤");

        for (Supplier supplier : suppliers) {
            String companyName = supplier.getCompanyName();
            String directorName = supplier.getDirectorName();
            String directorPhone = supplier.getDirectorPhone();
            int productCount = productCountBySupplier.getOrDefault(companyName, 0);

            // Обрезаем длинные строки для красивого отображения
            if (companyName.length() > 20) {
                companyName = companyName.substring(0, 17) + "...";
            }
            if (directorName.length() > 20) {
                directorName = directorName.substring(0, 17) + "...";
            }
            if (directorPhone.length() > 16) {
                directorPhone = directorPhone.substring(0, 13) + "...";
            }

            System.out.printf("│ %-20s │ %-20s │ %-16s │ %8d │%n",
                    companyName, directorName, directorPhone, productCount);
        }
        System.out.println("└──────────────────────┴──────────────────────┴──────────────────┴──────────┘");


    }
}

