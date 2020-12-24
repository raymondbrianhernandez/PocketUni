package raymondhernandez.pocketuniv.Model;

/**
 * Created 5/12/2016.
 */
public class Sell {

    private String item;
    private String type;
    private String notes;
    private int months;
    private double price;
//    private String addedBy;

    public Sell(){}

    public Sell(String item, String type, String notes, int months, double price) {
        this.item = item;
        this.type = type;
        this.notes = notes;
        this.months = months;
        this.price = price;
//        this.addedBy = addedBy;
    }

//    public String getAddedBy() {
//        return addedBy;
//    }
//
//    public void setAddedBy(String addedBy) {
//        this.addedBy = addedBy;
//    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public int getMonths() {
        return months;
    }

    public void setMonths(int months) {
        this.months = months;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
