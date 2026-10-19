package com.project.code.Controller;

import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ServiceClass serviceClass;

    @Autowired
    private InventoryRepository inventoryRepository;

    @PostMapping
    public Map<String, String> addProduct(@RequestBody Product product) {

        Map<String, String> response = new HashMap<>();

        try {
            if (!serviceClass.validateProduct(product)) {
                response.put("message", "Product already exists");
                return response;
            }

            productRepository.save(product);
            response.put("message", "Product added successfully");

        } catch (DataIntegrityViolationException exception) {
            response.put("message", "Product SKU already exists");

        } catch (Exception exception) {
            response.put("message", exception.getMessage());
        }

        return response;
    }

    @GetMapping("/{id}")
    public Map<String, Object> getProductbyId(@PathVariable long id) {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "products",
                productRepository.findById(id).orElse(null)
        );

        return response;
    }

    @PutMapping
    public Map<String, String> updateProduct(@RequestBody Product product) {

        Map<String, String> response = new HashMap<>();

        try {
            productRepository.save(product);
            response.put("message", "Product updated successfully");

        } catch (Exception exception) {
            response.put("message", exception.getMessage());
        }

        return response;
    }

    @GetMapping("/category/{name}/{category}")
    public Map<String, Object> filterbyCategoryProduct(
            @PathVariable String name,
            @PathVariable String category) {

        Map<String, Object> response = new HashMap<>();

        if (name.equalsIgnoreCase("null")
                && category.equalsIgnoreCase("null")) {

            response.put("products", productRepository.findAll());

        } else if (name.equalsIgnoreCase("null")) {

            response.put("products",
                    productRepository.findByCategory(category));

        } else if (category.equalsIgnoreCase("null")) {

            response.put("products",
                    productRepository.findProductBySubName(name));

        } else {

            response.put("products",
                    productRepository.findProductBySubNameAndCategory(
                            name,
                            category
                    ));
        }

        return response;
    }

    @GetMapping
    public Map<String, Object> listProduct() {

        Map<String, Object> response = new HashMap<>();
        response.put("products", productRepository.findAll());

        return response;
    }

    @GetMapping("/filter/{category}/{storeid}")
    public Map<String, Object> getProductbyCategoryAndStoreId(
            @PathVariable String category,
            @PathVariable long storeid) {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "product",
                productRepository.findProductByCategory(category, storeid)
        );

        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> deleteProduct(@PathVariable long id) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.ValidateProductId(id)) {
            response.put("message", "Product does not exist");
            return response;
        }

        inventoryRepository.deleteByProductId(id);
        productRepository.deleteById(id);

        response.put("message", "Product deleted successfully");

        return response;
    }

    @GetMapping("/searchProduct/{name}")
    public Map<String, Object> searchProduct(@PathVariable String name) {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "products",
                productRepository.findProductBySubName(name)
        );

        return response;
    }
}
