package com.example.mcpa_project_sem22.utils;

import android.content.Context;
import android.util.Log;
import com.example.mcpa_project_sem22.dao.ProductDAO;
import com.example.mcpa_project_sem22.dao.StoreDAO;
import com.example.mcpa_project_sem22.models.Product;
import com.example.mcpa_project_sem22.models.Store;
import java.util.ArrayList;
import java.util.List;

public class DataPopulator {
    private static final String TAG = "DataPopulator";
    private Context context;
    private ProductDAO productDAO;
    private StoreDAO storeDAO;

    public DataPopulator(Context context) {
        this.context = context;
        this.productDAO = new ProductDAO(context);
        this.storeDAO = new StoreDAO(context);
    }

    public void populateDatabase() {
        Log.d(TAG, "Starting database population...");
        
        // First populate stores, then categories, then products
        populateStores();
        populateCategories();
        populateProducts();
        
        Log.d(TAG, "Database population completed");
    }

    private void populateStores() {
        List<Store> stores = createSouthAfricanWoolworthsStores();
        
        for (Store store : stores) {
            long storeId = storeDAO.addStore(store);
            if (storeId > 0) {
                Log.d(TAG, "Added store: " + store.getName());
            } else {
                Log.e(TAG, "Failed to add store: " + store.getName());
            }
        }
    }

    private void populateCategories() {
        // Categories will be handled by the SQL script
        // This method can be expanded if needed for dynamic category creation
    }

    private void populateProducts() {
        List<Product> products = createSampleProducts();
        
        for (Product product : products) {
            long productId = productDAO.addProduct(product);
            if (productId > 0) {
                Log.d(TAG, "Added product: " + product.getName());
            } else {
                Log.e(TAG, "Failed to add product: " + product.getName());
            }
        }
    }

    private List<Product> createSampleProducts() {
        List<Product> products = new ArrayList<>();

        // Fresh Produce (Category ID: 1)
        products.add(createProduct("Bananas - 1kg", "Fresh yellow bananas, perfect for snacking", 24.99, 1, "Woolworths", "1kg", 50, "https://example.com/bananas.jpg"));
        products.add(createProduct("Apples - Red Delicious 1kg", "Crisp and sweet red delicious apples", 34.99, 1, "Woolworths", "1kg", 40, "https://example.com/apples.jpg"));
        products.add(createProduct("Carrots - 1kg", "Fresh orange carrots, great for cooking", 18.99, 1, "Woolworths", "1kg", 35, "https://example.com/carrots.jpg"));
        products.add(createProduct("Potatoes - 2kg", "Quality potatoes for all your cooking needs", 29.99, 1, "Woolworths", "2kg", 60, "https://example.com/potatoes.jpg"));
        products.add(createProduct("Tomatoes - 500g", "Fresh ripe tomatoes", 22.99, 1, "Woolworths", "500g", 30, "https://example.com/tomatoes.jpg"));

        // Dairy & Eggs (Category ID: 2)
        products.add(createProduct("Full Cream Milk - 2L", "Fresh full cream milk", 32.99, 2, "Woolworths", "2L", 25, "https://example.com/milk.jpg"));
        products.add(createProduct("Large Eggs - 18 Pack", "Free range large eggs", 54.99, 2, "Woolworths", "18 pack", 20, "https://example.com/eggs.jpg"));
        products.add(createProduct("Cheddar Cheese - 500g", "Mature cheddar cheese", 89.99, 2, "Woolworths", "500g", 15, "https://example.com/cheese.jpg"));
        products.add(createProduct("Greek Yogurt - 500g", "Creamy Greek style yogurt", 45.99, 2, "Woolworths", "500g", 18, "https://example.com/yogurt.jpg"));
        products.add(createProduct("Butter - 500g", "Salted butter", 67.99, 2, "Woolworths", "500g", 22, "https://example.com/butter.jpg"));

        // Meat & Poultry (Category ID: 3)
        products.add(createProduct("Chicken Breast - 1kg", "Fresh chicken breast fillets", 89.99, 3, "Woolworths", "1kg", 12, "https://example.com/chicken.jpg"));
        products.add(createProduct("Beef Mince - 500g", "Premium beef mince", 79.99, 3, "Woolworths", "500g", 15, "https://example.com/mince.jpg"));
        products.add(createProduct("Pork Chops - 1kg", "Fresh pork chops", 119.99, 3, "Woolworths", "1kg", 10, "https://example.com/pork.jpg"));
        products.add(createProduct("Lamb Chops - 500g", "Premium lamb chops", 149.99, 3, "Woolworths", "500g", 8, "https://example.com/lamb.jpg"));

        // Bakery (Category ID: 4)
        products.add(createProduct("White Bread - 700g", "Fresh white bread loaf", 18.99, 4, "Woolworths", "700g", 30, "https://example.com/bread.jpg"));
        products.add(createProduct("Croissants - 6 Pack", "Butter croissants", 34.99, 4, "Woolworths", "6 pack", 15, "https://example.com/croissants.jpg"));
        products.add(createProduct("Chocolate Muffins - 4 Pack", "Double chocolate muffins", 42.99, 4, "Woolworths", "4 pack", 20, "https://example.com/muffins.jpg"));

        // Pantry Essentials (Category ID: 5)
        products.add(createProduct("Basmati Rice - 2kg", "Premium basmati rice", 89.99, 5, "Tastic", "2kg", 25, "https://example.com/rice.jpg"));
        products.add(createProduct("Pasta - Penne 500g", "Durum wheat pasta", 24.99, 5, "Barilla", "500g", 40, "https://example.com/pasta.jpg"));
        products.add(createProduct("Olive Oil - 500ml", "Extra virgin olive oil", 79.99, 5, "Woolworths", "500ml", 18, "https://example.com/oil.jpg"));
        products.add(createProduct("Canned Tomatoes - 400g", "Whole peeled tomatoes", 16.99, 5, "All Gold", "400g", 50, "https://example.com/canned_tomatoes.jpg"));

        // Beverages (Category ID: 6)
        products.add(createProduct("Coca Cola - 2L", "Classic Coca Cola", 28.99, 6, "Coca Cola", "2L", 35, "https://example.com/coke.jpg"));
        products.add(createProduct("Orange Juice - 1L", "100% pure orange juice", 34.99, 6, "Woolworths", "1L", 20, "https://example.com/juice.jpg"));
        products.add(createProduct("Sparkling Water - 1L", "Natural sparkling water", 19.99, 6, "Woolworths", "1L", 30, "https://example.com/water.jpg"));
        products.add(createProduct("Coffee Beans - 250g", "Premium coffee beans", 89.99, 6, "Woolworths", "250g", 15, "https://example.com/coffee.jpg"));

        // Frozen Foods (Category ID: 7)
        products.add(createProduct("Frozen Peas - 1kg", "Garden peas", 32.99, 7, "Woolworths", "1kg", 25, "https://example.com/peas.jpg"));
        products.add(createProduct("Ice Cream - Vanilla 2L", "Premium vanilla ice cream", 67.99, 7, "Woolworths", "2L", 12, "https://example.com/icecream.jpg"));
        products.add(createProduct("Frozen Pizza - Margherita", "Wood fired pizza", 54.99, 7, "Woolworths", "each", 18, "https://example.com/pizza.jpg"));

        // Snacks & Confectionery (Category ID: 8)
        products.add(createProduct("Potato Chips - 200g", "Salted potato chips", 24.99, 8, "Simba", "200g", 40, "https://example.com/chips.jpg"));
        products.add(createProduct("Chocolate Bar - 80g", "Milk chocolate bar", 18.99, 8, "Cadbury", "80g", 60, "https://example.com/chocolate.jpg"));
        products.add(createProduct("Mixed Nuts - 500g", "Roasted mixed nuts", 89.99, 8, "Woolworths", "500g", 20, "https://example.com/nuts.jpg"));

        return products;
    }

    private Product createProduct(String name, String description, double price, int categoryId, 
                                String brand, String unit, int stock, String imageUrl) {
        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setCategoryId(categoryId);
        product.setBrand(brand);
        product.setUnit(unit);
        product.setStockQuantity(stock);
        product.setImageUrl(imageUrl);
        product.setActive(true);
        product.setMinOrderQuantity(1);
        product.setMaxOrderQuantity(10);
        
        // Add some random discounts to make it more realistic
        if (Math.random() < 0.3) { // 30% chance of discount
            double discountPercent = 5 + (Math.random() * 20); // 5-25% discount
            double discountedPrice = price * (1 - discountPercent / 100);
            product.setDiscountedPrice(Math.round(discountedPrice * 100.0) / 100.0);
        }
        
        return product;
    }

    private List<Store> createSouthAfricanWoolworthsStores() {
        List<Store> stores = new ArrayList<>();

        // Gauteng Province
        stores.add(createStore("Woolworths Sandton City", "83 Rivonia Rd, Sandhurst", "Sandton", "Gauteng", 
                "2196", "011 217 6000", "sandtoncity@woolworths.co.za", -26.1076, 28.0567, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Mall of Africa", "Lone Creek Crescent, Waterfall City", "Midrand", "Gauteng", 
                "1685", "011 549 4000", "mallafrica@woolworths.co.za", -25.9269, 28.1186, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Menlyn Park", "Cnr Atterbury & Lois Ave", "Pretoria", "Gauteng", 
                "0181", "012 348 4500", "menlynpark@woolworths.co.za", -25.7847, 28.2774, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Eastgate", "43 Bradford Rd", "Bedfordview", "Gauteng", 
                "2008", "011 615 2300", "eastgate@woolworths.co.za", -26.1667, 28.1667, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Rosebank", "50 Bath Ave", "Rosebank", "Gauteng", 
                "2196", "011 447 9600", "rosebank@woolworths.co.za", -26.1447, 28.0436, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // Western Cape Province
        stores.add(createStore("Woolworths V&A Waterfront", "Victoria Wharf Shopping Centre", "Cape Town", "Western Cape", 
                "8001", "021 408 7000", "waterfront@woolworths.co.za", -33.9022, 18.4194, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Canal Walk", "Century Blvd", "Century City", "Western Cape", 
                "7441", "021 555 4000", "canalwalk@woolworths.co.za", -33.8906, 18.5122, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Cavendish Square", "Dreyer St", "Claremont", "Western Cape", 
                "7708", "021 657 5000", "cavendish@woolworths.co.za", -33.9833, 18.4667, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Tyger Valley", "Cnr Willie van Schoor & Old Oak Rd", "Bellville", "Western Cape", 
                "7530", "021 914 4000", "tygervalley@woolworths.co.za", -33.9147, 18.6331, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Constantia Village", "Constantia Main Rd", "Constantia", "Western Cape", 
                "7806", "021 794 7500", "constantia@woolworths.co.za", -34.0333, 18.4167, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // KwaZulu-Natal Province
        stores.add(createStore("Woolworths Gateway", "1 Palm Blvd", "Umhlanga", "KwaZulu-Natal", 
                "4319", "031 566 2000", "gateway@woolworths.co.za", -29.7281, 31.0669, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Pavilion", "Jack Martens Dr", "Westville", "KwaZulu-Natal", 
                "3629", "031 265 0900", "pavilion@woolworths.co.za", -29.8333, 30.9167, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths La Lucia Mall", "Cnr William Campbell & Douglas Saunders Dr", "La Lucia", "KwaZulu-Natal", 
                "4051", "031 572 0300", "lalucia@woolworths.co.za", -29.7667, 31.0833, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // Eastern Cape Province
        stores.add(createStore("Woolworths Greenacres", "Cnr 2nd Ave & Heugh Rd", "Port Elizabeth", "Eastern Cape", 
                "6045", "041 363 2000", "greenacres@woolworths.co.za", -33.9667, 25.6167, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        stores.add(createStore("Woolworths Hemingways Mall", "Cnr Kragga Kamma & Doncaster Rd", "Port Elizabeth", "Eastern Cape", 
                "6001", "041 581 2500", "hemingways@woolworths.co.za", -33.9833, 25.5167, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // Free State Province
        stores.add(createStore("Woolworths Mimosa Mall", "Dan Pienaar Ave", "Bloemfontein", "Free State", 
                "9301", "051 444 6000", "mimosa@woolworths.co.za", -29.0833, 26.1667, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // Mpumalanga Province
        stores.add(createStore("Woolworths Riverside Mall", "Cnr R40 & N4", "Nelspruit", "Mpumalanga", 
                "1200", "013 741 2000", "riverside@woolworths.co.za", -25.4667, 30.9833, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // Limpopo Province
        stores.add(createStore("Woolworths Mall of the North", "Cnr N1 & R101", "Polokwane", "Limpopo", 
                "0699", "015 297 5000", "mallnorth@woolworths.co.za", -23.9167, 29.4667, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // North West Province
        stores.add(createStore("Woolworths Mafikeng Mall", "University Dr", "Mafikeng", "North West", 
                "2745", "018 381 2000", "mafikeng@woolworths.co.za", -25.8167, 25.6333, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        // Northern Cape Province
        stores.add(createStore("Woolworths Kimberly", "Memorial Rd", "Kimberley", "Northern Cape", 
                "8301", "053 832 1000", "kimberley@woolworths.co.za", -28.7333, 24.7667, 
                "Mon-Thu: 9:00-21:00, Fri-Sat: 9:00-22:00, Sun: 9:00-19:00", "Supermarket"));

        return stores;
    }

    private Store createStore(String name, String address, String city, String province, 
                            String postalCode, String phone, String email, double latitude, 
                            double longitude, String openingHours, String storeType) {
        Store store = new Store();
        store.setName(name);
        store.setAddress(address);
        store.setCity(city);
        store.setProvince(province);
        store.setPostalCode(postalCode);
        store.setPhone(phone);
        store.setEmail(email);
        store.setLatitude(latitude);
        store.setLongitude(longitude);
        store.setOpeningHours(openingHours);
        store.setStoreType(storeType);
        store.setActive(true);
        store.setImageUrl("https://example.com/store_images/" + name.toLowerCase().replace(" ", "_") + ".jpg");
        return store;
    }
}