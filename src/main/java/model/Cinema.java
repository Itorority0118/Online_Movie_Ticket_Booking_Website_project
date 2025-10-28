package model;

public class Cinema {
    
    private int cinemaId;
    private String name;
    private String address;
    private String city;
    private String phone;

    // Empty constructor
    public Cinema() {}

    // Constructor
    public Cinema(int cinemaId, String name, String address, String city, String phone) {
        this.cinemaId = cinemaId;
        this.name = name;
        this.address = address;
        this.city = city;
        this.phone = phone;
    }

    // Getters
    public int getCinemaId() { return cinemaId; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getPhone() { return phone; }

    // Setters
    public void setCinemaId(int cinemaId) { this.cinemaId = cinemaId; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setPhone(String phone) { this.phone = phone; }
}