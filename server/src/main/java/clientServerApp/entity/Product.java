package clientServerApp.entity;

public class Product {
    private String name;
    private double price;
    private int amount;
    private String description;
    private String producer;
    private Integer group_id;

    public Product(String name, double price, int amount, String description, String producer, Integer group_id) {
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.description = description;
        this.producer = producer;
        this.group_id = group_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String manufacturer) {
        this.producer = manufacturer;
    }

    public Integer getGroup_id() {
        return group_id;
    }

    public void setGroup_id(Integer group_id) {
        this.group_id = group_id;
    }

    public String toString(){
        return "{ name: " + name +
                ", price: " + price +
                ", amount: " + amount +
                ", description: " + description +
                ", producer: " + producer +
                ", group_id: " + group_id + " }";
    }
}