import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "products")
public class Product {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField(canBeNull = false)
    private String name;

    @DatabaseField
    private String supplier;

    @DatabaseField
    private String category;

    @DatabaseField
    private double cost;

    @DatabaseField
    private int amount;

    public Product() {
        // ORMLite требует пустой конструктор
    }

    public Product(int id, String name, String supplier, String category, double cost, int amount) {
        this.id = id;
        this.name = name;
        this.supplier = supplier;
        this.category = category;
        this.cost = cost;
        this.amount = amount;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getSupplier() { return supplier; }
    public void setSupplier(String supplier) { this.supplier = supplier; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public double getCost() { return cost; }
    public void setCost(double cost) { this.cost = cost; }

    public int getAmount() { return amount; }
    public void setAmount(int amount) { this.amount = amount; }

    @Override
    public String toString() {
        return String.format("id = %d, name = '%s', supplier = '%s', category = '%s', cost = %.2f, amount = %d",
                id, name, supplier, category, cost, amount);
    }
}