import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.Level;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.nio.file.*;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class DatabaseInitializer {
    private static final String DB_URL = "jdbc:h2:./Database/products;DB_CLOSE_DELAY=-1";
    private static final String USER = "sa";
    private static final String PASS = "";

    private Dao<Product, Integer> productDao;
    private Dao<Supplier, Integer> supplierDao;
    private ConnectionSource connectionSource;

    static void main(String[] args) {
        new DatabaseInitializer().run();
    }


    public void run() {
        try {
            Logger.setGlobalLogLevel(Level.OFF);
            connectionSource = new JdbcConnectionSource(DB_URL, USER, PASS);

            productDao = DaoManager.createDao(connectionSource, Product.class);
            supplierDao = DaoManager.createDao(connectionSource, Supplier.class);

            TableUtils.createTableIfNotExists(connectionSource, Supplier.class);
            TableUtils.createTableIfNotExists(connectionSource, Product.class);

            clearExistingData();
            insertSuppliersFromFile();
            insertProductsFromFile();

            System.out.println("\n=== ВСЕ ПОСТАВЩИКИ ===");
            displaySuppliers();

            System.out.println("\n=== ВСЕ ТОВАРЫ ===");
            displayProducts();

            displaySupplierContacts();
            demonstrateDaoOperations();

        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connectionSource != null) {
                try {
                    connectionSource.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void clearExistingData() throws Exception {
        productDao.deleteBuilder().delete();
        supplierDao.deleteBuilder().delete();
        System.out.println("Существующие данные очищены");
    }

    private void insertSuppliersFromFile() throws Exception {
        List<Supplier> suppliers = readSuppliersFromFile("suppliers.txt");
        for (Supplier supplier : suppliers) {
            supplierDao.create(supplier);
        }
        System.out.println("Добавлено " + suppliers.size() + " поставщиков из файла suppliers.txt");
    }

    private void insertProductsFromFile() throws Exception {
        List<Product> products = readProductsFromFile("data.txt");
        for (Product product : products) {
            productDao.create(product);
        }
        System.out.println("Добавлено " + products.size() + " товаров из файла data.txt");
    }

    private List<Supplier> readSuppliersFromFile(String filename) throws IOException {
        List<Supplier> suppliers = new ArrayList<>();
        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            System.err.println("Файл " + filename + " не найден!");
            return suppliers;
        }

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] parts = line.split(", ");
            if (parts.length == 5) {
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String companyName = parts[1].trim();
                    String directorName = parts[2].trim();
                    String directorPhone = parts[3].trim();
                    String directorEmail = parts[4].trim();

                    suppliers.add(new Supplier(id, companyName, directorName, directorPhone, directorEmail));
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка парсинга строки: " + line);
                }
            } else {
                System.err.println("Некорректный формат строки: " + line);
            }
        }
        return suppliers;
    }

    private List<Product> readProductsFromFile(String filename) throws IOException {
        List<Product> products = new ArrayList<>();
        Path path = Paths.get(filename);

        if (!Files.exists(path)) {
            System.err.println("Файл " + filename + " не найден!");
            return products;
        }

        List<String> lines = Files.readAllLines(path);
        for (String line : lines) {
            String[] parts = line.split(", ");
            if (parts.length == 6) {
                try {
                    int id = Integer.parseInt(parts[0].trim());
                    String name = parts[1].trim();
                    String supplier = parts[2].trim();
                    String category = parts[3].trim();
                    double cost = Double.parseDouble(parts[4].trim());
                    int amount = Integer.parseInt(parts[5].trim());

                    products.add(new Product(id, name, supplier, category, cost, amount));
                } catch (NumberFormatException e) {
                    System.err.println("Ошибка парсинга строки: " + line);
                }
            } else {
                System.err.println("Некорректный формат строки: " + line);
            }
        }
        return products;
    }

    private void displayProducts() throws Exception {
        List<Product> products = productDao.queryForAll();

        System.out.println("┌────┬────────────────────────────────┬──────────────────────┬──────────────────────┬──────────┬────────┐");
        System.out.println("│ ID │ Наименование                   │ Поставщик            │ Категория            │   Цена   │ Кол-во │");
        System.out.println("├────┼────────────────────────────────┼──────────────────────┼──────────────────────┼──────────┼────────┤");

        for (Product product : products) {
            String name = product.getName();
            String supplier = product.getSupplier();
            String category = product.getCategory();

            System.out.printf("│ %-2d │ %-30s │ %-20s │ %-20s │ %8.2f │ %6d │%n",
                    product.getId(), name, supplier, category, product.getCost(), product.getAmount());
        }
        System.out.println("└────┴────────────────────────────────┴──────────────────────┴──────────────────────┴──────────┴────────┘");

        displayAllProductsStatistics(products);
    }

    private void displayAllProductsStatistics(List<Product> products) {
        int totalProducts = products.size();
        double totalValue = products.stream().mapToDouble(p -> p.getCost() * p.getAmount()).sum();
        long uniqueCategories = products.stream().map(Product::getCategory).distinct().count();
        long uniqueSuppliers = products.stream().map(Product::getSupplier).distinct().count();

        System.out.printf("\nОбщая статистика по товарам:");
        System.out.printf("\nВсего товаров: %d", totalProducts);
        System.out.printf("\nУникальных категорий: %d", uniqueCategories);
        System.out.printf("\nУникальных поставщиков: %d", uniqueSuppliers);
        System.out.printf("\nОбщая стоимость: %.2f руб.\n\n", totalValue);
    }

    private void displaySuppliers() throws Exception {
        List<Supplier> suppliers = supplierDao.queryForAll();

        System.out.println("┌────┬──────────────────────┬──────────────────────┬──────────────────┬──────────────────────────────┐");
        System.out.println("│ ID │ Компания             │ Директор             │ Телефон          │ Email                        │");
        System.out.println("├────┼──────────────────────┼──────────────────────┼──────────────────┼──────────────────────────────┤");

        for (Supplier supplier : suppliers) {
            String companyName = supplier.getCompanyName();
            String directorName = supplier.getDirectorName();
            String directorPhone = supplier.getDirectorPhone();
            String directorEmail = supplier.getDirectorEmail();

            System.out.printf("│ %-2d │ %-20s │ %-20s │ %-16s │ %-29s│%n",
                    supplier.getId(), companyName, directorName, directorPhone, directorEmail);
        }
        System.out.println("└────┴──────────────────────┴──────────────────────┴──────────────────┴──────────────────────────────┘");

        displaySuppliersStatistics(suppliers);
    }

    private void displaySuppliersStatistics(List<Supplier> suppliers) {
        int totalSuppliers = suppliers.size();
        System.out.printf("\nСтатистика по поставщикам:");
        System.out.printf("\nВсего поставщиков: %d\n", totalSuppliers);
    }

    private void displaySupplierContacts() throws Exception {
        List<Supplier> suppliers = supplierDao.queryForAll();
        List<Product> products = productDao.queryForAll();

        Map<String, Integer> productCountBySupplier = products.stream()
                .collect(Collectors.groupingBy(
                        Product::getSupplier,
                        Collectors.summingInt(p -> 1)
                ));

        System.out.println("\n=== КОНТАКТЫ ПОСТАВЩИКОВ И СТАТИСТИКА ===");
        System.out.println("┌──────────────────────┬──────────────────────┬──────────────────┬──────────┐");
        System.out.println("│ Компания             │ Директор             │ Телефон          │ Товаров  │");
        System.out.println("├──────────────────────┼──────────────────────┼──────────────────┼──────────┤");

        for (Supplier supplier : suppliers) {
            String companyName = supplier.getCompanyName();
            String directorName = supplier.getDirectorName();
            String directorPhone = supplier.getDirectorPhone();
            int productCount = productCountBySupplier.getOrDefault(companyName, 0);

            System.out.printf("│ %-20s │ %-20s │ %-16s │ %8d │%n",
                    companyName, directorName, directorPhone, productCount);
        }
        System.out.println("└──────────────────────┴──────────────────────┴──────────────────┴──────────┘");
    }

    private void demonstrateDaoOperations() throws Exception {
        System.out.println("\n=== ДЕМОНСТРАЦИЯ ОПЕРАЦИЙ DAO ===");

        // Поиск товара по ID
        Product product = productDao.queryForId(1);
        if (product != null) {
            System.out.println("Найден товар по ID 1: " + product.getName());
        }

        // Поиск поставщика по ID
        Supplier supplier = supplierDao.queryForId(1);
        if (supplier != null) {
            System.out.println("Найден поставщик по ID 1: " + supplier.getCompanyName());
        }

        // Поиск товаров по категории с помощью QueryBuilder
        List<Product> laptops = productDao.queryBuilder()
                .where()
                .eq("category", "Ноутбук")
                .query();
        for (Product laptop : laptops)
            System.out.println("Найдененный ноутбук: " + laptop.toString());

        // Обновление товара
        if (product != null) {
            double oldCost = product.getCost();
            product.setCost(oldCost + 1000);
            productDao.update(product);
            System.out.printf("Цена товара '%s' обновлена с %.2f до %.2f\n",
                    product.getName(), oldCost, product.getCost());
        }

        // Подсчет общего количества товаров
        long totalProducts = productDao.countOf();
        System.out.println("Общее количество товаров в базе: " + totalProducts);

        // Получение товаров с сортировкой
        List<Product> sortedProducts = productDao.queryBuilder()
                .orderBy("cost", false) // Сортировка по убыванию цены
                .limit(3L) // Только 3 записи
                .query();
        System.out.println("\nТоп-3 самых дорогих товаров:");
        for (Product p : sortedProducts) {
            System.out.printf("  %s - %.2f руб.\n", p.getName(), p.getCost());
        }
    }
}