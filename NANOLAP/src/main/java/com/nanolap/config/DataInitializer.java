package com.nanolap.config;

import com.nanolap.model.Product;
import com.nanolap.model.User;
import com.nanolap.repository.ProductRepository;
import com.nanolap.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        // Initialize default users
        if (userRepository.count() == 0) {
            List<User> users = Arrays.asList(
                createUser("Admin User", "admin@nanolap.lk", "admin123", "admin"),
                createUser("John Customer", "customer@nanolap.lk", "customer123", "customer")
            );
            userRepository.saveAll(users);
            System.out.println("✅ NANOLAP: Default users loaded successfully!");
            System.out.println("   Admin: admin@nanolap.lk / admin123");
            System.out.println("   Customer: customer@nanolap.lk / customer123");
        }

        // Initialize products
        if (productRepository.count() == 0) {
            List<Product> products = Arrays.asList(
                createProduct("Samsung Galaxy S24 Ultra", "Latest Samsung flagship with 200MP camera and AI features",
                    new BigDecimal("189999"), new BigDecimal("209999"), "Smartphones", "Samsung",
                    "https://images.samsung.com/is/image/samsung/p6pim/global/2401/gallery/global-galaxy-s24-ultra-s928-sm-s928bztgtgy-thumb-539573052", 25, 4.8, 1250, true),
                createProduct("Apple iPhone 15 Pro", "A17 Pro chip, titanium design, 48MP camera",
                    new BigDecimal("229999"), new BigDecimal("249999"), "Smartphones", "Apple",
                    "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/iphone-15-pro-finish-select-202309-6-1inch-naturaltitanium", 18, 4.9, 2100, true),
                createProduct("Dell XPS 15 Laptop", "15.6\" OLED, Intel i9, 32GB RAM, 1TB SSD",
                    new BigDecimal("359999"), new BigDecimal("399999"), "Laptops", "Dell",
                    "https://i.dell.com/is/image/DellContent/content/dam/ss2/product-images/dell-client-products/notebooks/xps-notebooks/xps-15-9530/media-gallery/black/notebook-xps-15-9530-t-black-gallery-1.psd", 12, 4.7, 890, true),
                createProduct("MacBook Pro 14\" M3", "M3 chip, Liquid Retina XDR display, 18-hour battery",
                    new BigDecimal("549999"), new BigDecimal("599999"), "Laptops", "Apple",
                    "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/mbp14-spacegray-select-202310", 8, 4.9, 1560, true),
                createProduct("Sony WH-1000XM5", "Industry-leading noise cancellation wireless headphones",
                    new BigDecimal("59999"), new BigDecimal("69999"), "Audio", "Sony",
                    "https://sony.scene7.com/is/image/sonyglobalsolutions/WH-1000XM5_Black_Primary_Image", 35, 4.8, 3200, true),
                createProduct("iPad Pro 12.9\" M2", "Apple M2 chip, Ultra Retina XDR display, Apple Pencil support",
                    new BigDecimal("189999"), new BigDecimal("209999"), "Tablets", "Apple",
                    "https://store.storeimages.cdn-apple.com/4982/as-images.apple.com/is/ipad-pro-13-select-wifi-spacegray-202210", 15, 4.8, 780, true),
                createProduct("Samsung 65\" 4K QLED TV", "Neo QLED with Quantum HDR 32x, 144Hz Gaming",
                    new BigDecimal("289999"), new BigDecimal("349999"), "TVs", "Samsung",
                    "https://images.samsung.com/is/image/samsung/p6pim/global/qn65qn900cfxza/gallery/global-neo-qled-8k-65-qn900c-qn65qn900cfxza-537271580", 6, 4.7, 420, true),
                createProduct("Logitech MX Master 3S", "Advanced wireless mouse with 8K DPI optical sensor",
                    new BigDecimal("14999"), new BigDecimal("17999"), "Accessories", "Logitech",
                    "https://resource.logitech.com/content/dam/logitech/en/products/mice/mx-master-3s/gallery/mx-master-3s-mouse-top-view-graphite.png", 50, 4.7, 5600, false),
                createProduct("ASUS ROG Gaming Monitor 27\"", "27\" 2K 165Hz IPS panel, G-Sync Compatible",
                    new BigDecimal("89999"), new BigDecimal("99999"), "Monitors", "ASUS",
                    "https://dlcdnwebimgs.asus.com/gain/a1ff3c9b-3b97-4d90-acf0-e5e9af47b53b/w800/h600", 10, 4.6, 670, false),
                createProduct("DJI Mini 4 Pro Drone", "4K/60fps camera, omnidirectional obstacle sensing",
                    new BigDecimal("199999"), new BigDecimal("229999"), "Cameras", "DJI",
                    "https://store.dji.com/product/dji-mini-4-pro", 7, 4.8, 340, true),
                createProduct("Razer Keyboard BlackWidow V4", "Mechanical gaming keyboard with Razer Green switches",
                    new BigDecimal("24999"), new BigDecimal("29999"), "Accessories", "Razer",
                    "https://assets3.razerzone.com/I6X1gqxzDX0V9jEJZwxL2A5P02U=/1500x1000/https://hybrismediaprod.blob.core.windows.net/sys-master-phoenix-images-container/h5e/hb2/9397048680478/razer-blackwidow-v4-500x500.png", 20, 4.5, 890, false),
                createProduct("Google Pixel 8 Pro", "Google AI phone with 50MP camera and 7 years updates",
                    new BigDecimal("139999"), new BigDecimal("159999"), "Smartphones", "Google",
                    "https://lh3.googleusercontent.com/jMJbNxFmxCoJQFsRpgBCPKjPLiJPo0JG5-xHE0oKMRWLnXZSO2Xn7HJPq8S43bpjChh0QkKQmqfFGmJ=rw-e365", 22, 4.6, 1100, false)
            );
            productRepository.saveAll(products);
            System.out.println("✅ NANOLAP: Sample products loaded successfully!");
        }
    }

    private Product createProduct(String name, String desc, BigDecimal price, BigDecimal origPrice,
                                   String category, String brand, String imageUrl,
                                   int stock, double rating, int reviews, boolean featured) {
        Product p = new Product();
        p.setName(name);
        p.setDescription(desc);
        p.setPrice(price);
        p.setOriginalPrice(origPrice);
        p.setCategory(category);
        p.setBrand(brand);
        p.setImageUrl(imageUrl);
        p.setStockQuantity(stock);
        p.setRating(rating);
        p.setReviewCount(reviews);
        p.setIsFeatured(featured);
        p.setIsActive(true);
        return p;
    }

    private User createUser(String name, String email, String password, String role) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);
        user.setRole(role);
        return user;
    }
}
