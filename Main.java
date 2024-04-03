package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SpringBootApplication
public class Main {

    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

@RestController
class ProductController {
    private List<Product> products = new ArrayList<>();

    public ProductController() {
        generateSampleProducts();
    }

    private void generateSampleProducts() {
        String[] companies = {"AMZ", "FLP", "SNP", "MYN", "AZO"};
        String[] categories = {"phone", "computer", "tv", "earphone", "tablet", "charger", "mouse",
                "keypad", "bluetooth", "pendrive", "remote", "speaker", "headset",
                "laptop", "PC"};

        int id = 1;
        for (String company : companies) {
            for (String category : categories) {
                products.add(new Product(id++, "Product " + id, company, category, id * 100));
            }
        }
    }

    @GetMapping("/products")
    public List<Product> getTopProducts(@RequestParam String company,
                                        @RequestParam double minPrice,
                                        @RequestParam double maxPrice,
                                        @RequestParam int n) {

        List<Product> filteredProducts = products.stream()
                .filter(p -> p.getCompany().equals(company) && p.getPrice() >= minPrice && p.getPrice() <= maxPrice)
                .toList();

        return filteredProducts.stream()
                .sorted((p1, p2) -> Double.compare(p2.getPrice(), p1.getPrice()))
                .limit(n)
                .collect(Collectors.toList());
    }

    @PostMapping("/products/post")
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        // Assign a new ID to the new product
        product.setId(products.size() + 1);

        // Add the product to the list
        products.add(product);

        return new ResponseEntity<>("Product added successfully", HttpStatus.CREATED);
    }
}

class Product {
    private int id;
    private String name;
    private String company;
    private String category;
    private double price;

    public Product(int id, String name, String company, String category, double price) {
        this.id = id;
        this.name = name;
        this.company = company;
        this.category = category;
        this.price = price;
    }

    public String getCompany() {
        return company;
    }

    public int getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Stream<Object> stream() {
        return null;
    }
}
