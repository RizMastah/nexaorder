package com.raihan.nexaorder.config;

import com.raihan.nexaorder.entity.Product;
import com.raihan.nexaorder.entity.User;
import com.raihan.nexaorder.enums.UserRole;
import com.raihan.nexaorder.repository.ProductRepository;
import com.raihan.nexaorder.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner seedDefaultData(UserRepository userRepository, ProductRepository productRepository) {
        return args -> {
            String adminEmail = "admin@nexaorder.com";

            if (!userRepository.existsByEmail(adminEmail)) {
                User admin = User.builder()
                        .name("NexaOrder Admin")
                        .email(adminEmail)
                        .password("admin123")
                        .role(UserRole.ADMIN)
                        .build();

                userRepository.save(admin);
            }

            String customerEmail = "customer@nexaorder.com";
            if (!userRepository.existsByEmail(customerEmail)) {
                User customer = User.builder()
                        .name("Ariel Hikmat")
                        .email(customerEmail)
                        .password("customer123")
                        .role(UserRole.CUSTOMER)
                        .build();

                userRepository.save(customer);
            } else {
                userRepository.findByEmail(customerEmail)
                        .filter(user -> "NexaOrder Customer".equalsIgnoreCase(user.getName()))
                        .ifPresent(user -> {
                            user.setName("Ariel Hikmat");
                            userRepository.save(user);
                        });
            }

            if (productRepository.count() == 0) {
                productRepository.saveAll(List.of(
                        product("Wireless Mouse", "Accessories", "129000", 24,
                                "Compact wireless mouse with quiet clicks and reliable battery life for daily office work."),
                        product("Mechanical Keyboard", "Accessories", "589000", 12,
                                "Tactile mechanical keyboard built for long typing sessions and small business workstations."),
                        product("USB-C Cable", "Electronics", "49000", 40,
                                "Durable USB-C charging and data cable for phones, tablets, and laptop accessories."),
                        product("Laptop Stand", "Office Supplies", "179000", 18,
                                "Adjustable aluminum laptop stand that improves desk ergonomics and airflow."),
                        product("Bluetooth Speaker", "Electronics", "349000", 9,
                                "Portable speaker with clear sound for meeting rooms, storefronts, and home offices."),
                        product("Notebook", "Office Supplies", "35000", 50,
                                "A5 ruled notebook for daily notes, order lists, and quick planning."),
                        product("Desk Lamp", "Home & Living", "225000", 15,
                                "LED desk lamp with adjustable brightness for focused work areas."),
                        product("Webcam", "Electronics", "399000", 7,
                                "Full HD webcam for online meetings, product demos, and remote sales calls."),
                        product("Office Chair", "Home & Living", "1250000", 5,
                                "Ergonomic office chair with adjustable height and supportive backrest."),
                        product("Power Bank", "Electronics", "275000", 20,
                                "High-capacity power bank for mobile teams and travel-ready charging.")
                ));
            }
        };
    }

    private Product product(String name, String category, String price, Integer stock, String description) {
        return Product.builder()
                .name(name)
                .category(category)
                .price(new BigDecimal(price))
                .stock(stock)
                .description(description)
                .build();
    }
}
