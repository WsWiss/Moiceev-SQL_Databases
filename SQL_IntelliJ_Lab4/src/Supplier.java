import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "suppliers")
public class Supplier {
    @DatabaseField(id = true)
    private int id;

    @DatabaseField(columnName = "company_name", canBeNull = false)
    private String companyName;

    @DatabaseField(columnName = "director_name")
    private String directorName;

    @DatabaseField(columnName = "director_phone")
    private String directorPhone;

    @DatabaseField(columnName = "director_email")
    private String directorEmail;

    public Supplier() {
        // ORMLite требует пустой конструктор
    }

    public Supplier(int id, String companyName, String directorName, String directorPhone, String directorEmail) {
        this.id = id;
        this.companyName = companyName;
        this.directorName = directorName;
        this.directorPhone = directorPhone;
        this.directorEmail = directorEmail;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getDirectorName() { return directorName; }
    public void setDirectorName(String directorName) { this.directorName = directorName; }

    public String getDirectorPhone() { return directorPhone; }
    public void setDirectorPhone(String directorPhone) { this.directorPhone = directorPhone; }

    public String getDirectorEmail() { return directorEmail; }
    public void setDirectorEmail(String directorEmail) { this.directorEmail = directorEmail; }

    @Override
    public String toString() {
        return String.format("Supplier{id=%d, companyName='%s', directorName='%s', directorPhone='%s', directorEmail='%s'}",
                id, companyName, directorName, directorPhone, directorEmail);
    }
}