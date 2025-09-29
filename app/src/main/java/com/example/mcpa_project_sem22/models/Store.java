package com.example.mcpa_project_sem22.models;

public class Store {
    private int id;
    private String name;
    private String address;
    private String city;
    private String province;
    private String postalCode;
    private String phone;
    private String email;
    private double latitude;
    private double longitude;
    private String openingHours;
    private boolean isActive;
    private String storeType; // e.g., "Supermarket", "Food", "Clothing"
    private String imageUrl;

    // Default constructor
    public Store() {
        this.isActive = true;
    }

    // Constructor with essential fields (maintaining backward compatibility)
    public Store(int id, String name, String address, String phone, double latitude, double longitude) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.phone = phone;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = true;
    }

    // Constructor with location details
    public Store(String name, String address, String city, String province, 
                 double latitude, double longitude) {
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.latitude = latitude;
        this.longitude = longitude;
        this.isActive = true;
    }

    // Full constructor
    public Store(int id, String name, String address, String city, String province,
                 String postalCode, String phone, String email, double latitude, 
                 double longitude, String openingHours, boolean isActive, String storeType, String imageUrl) {
        this.id = id;
        this.name = name;
        this.address = address;
        this.city = city;
        this.province = province;
        this.postalCode = postalCode;
        this.phone = phone;
        this.email = email;
        this.latitude = latitude;
        this.longitude = longitude;
        this.openingHours = openingHours;
        this.isActive = isActive;
        this.storeType = storeType;
        this.imageUrl = imageUrl;
    }

    // Getters
    public int getId() { return id; }
    public String getName() { return name; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getProvince() { return province; }
    public String getPostalCode() { return postalCode; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }
    public String getOpeningHours() { return openingHours; }
    public boolean isActive() { return isActive; }
    public String getStoreType() { return storeType; }
    public String getImageUrl() { return imageUrl; }

    // Setters
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAddress(String address) { this.address = address; }
    public void setCity(String city) { this.city = city; }
    public void setProvince(String province) { this.province = province; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setLatitude(double latitude) { this.latitude = latitude; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setOpeningHours(String openingHours) { this.openingHours = openingHours; }
    public void setActive(boolean active) { isActive = active; }
    public void setStoreType(String storeType) { this.storeType = storeType; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    // Utility methods
    public String getFullAddress() {
        StringBuilder fullAddress = new StringBuilder();
        fullAddress.append(address);
        if (city != null && !city.isEmpty()) {
            fullAddress.append(", ").append(city);
        }
        if (province != null && !province.isEmpty()) {
            fullAddress.append(", ").append(province);
        }
        if (postalCode != null && !postalCode.isEmpty()) {
            fullAddress.append(" ").append(postalCode);
        }
        return fullAddress.toString();
    }

    public double distanceTo(double lat, double lon) {
        // Calculate distance using Haversine formula
        final int R = 6371; // Radius of the earth in km
        
        double latDistance = Math.toRadians(lat - this.latitude);
        double lonDistance = Math.toRadians(lon - this.longitude);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(this.latitude)) * Math.cos(Math.toRadians(lat))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c; // Distance in km
    }

    @Override
    public String toString() {
        return "Store{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", city='" + city + '\'' +
                ", province='" + province + '\'' +
                ", isActive=" + isActive +
                '}';
    }
}