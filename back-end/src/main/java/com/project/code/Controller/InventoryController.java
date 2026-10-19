package com.project.code.Controller;

import com.project.code.Model.CombinedRequest;
import com.project.code.Model.Inventory;
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
@RequestMapping("/inventory")
public class InventoryController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private ServiceClass serviceClass;

    @PutMapping
    public Map<String, String> updateInventory(
            @RequestBody CombinedRequest combinedRequest) {

        Map<String, String> response = new HashMap<>();

        try {
            long productId = combinedRequest.getProduct().getId();

            if (!serviceClass.ValidateProductId(productId)) {
                response.put("message", "Product not present in database");
                return response;
            }

            Inventory inventory = combinedRequest.getInventory();

            if (serviceClass.validateInventory(inventory)) {
                productRepository.save(combinedRequest.getProduct());
                inventoryRepository.save(inventory);

                response.put("message", "Successfully updated product");
            } else {
                response.put("message", "No data available");
            }

        } catch (DataIntegrityViolationException exception) {
            response.put("message", "Unable to update product data");

        } catch (Exception exception) {
            response.put("message", exception.getMessage());
        }

        return response;
    }

    @PostMapping
    public Map<String, String> saveInventory(@RequestBody Inventory inventory) {

        Map<String, String> response = new HashMap<>();

        try {
            if (serviceClass.validateInventory(inventory)) {
                response.put("message", "Data is already present");
                return response;
            }

            inventoryRepository.save(inventory);
            response.put("message", "Data saved successfully");

        } catch (DataIntegrityViolationException exception) {
            response.put("message", "Unable to save inventory data");

        } catch (Exception exception) {
            response.put("message", exception.getMessage());
        }

        return response;
    }

    @GetMapping("/{storeid}")
    public Map<String, Object> getAllProducts(
            @PathVariable long storeid) {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "products",
                productRepository.findProductsByStoreId(storeid)
        );

        return response;
    }

    @GetMapping("/filter/{category}/{name}/{storeid}")
    public Map<String, Object> getProductName(
            @PathVariable String category,
            @PathVariable String name,
            @PathVariable long storeid) {

        Map<String, Object> response = new HashMap<>();

        if (category.equalsIgnoreCase("null")
                && name.equalsIgnoreCase("null")) {

            response.put(
                    "product",
                    productRepository.findProductsByStoreId(storeid)
            );

        } else if (category.equalsIgnoreCase("null")) {

            response.put(
                    "product",
                    productRepository.findByNameLike(storeid, name)
            );

        } else if (name.equalsIgnoreCase("null")) {

            response.put(
                    "product",
                    productRepository.findByCategoryAndStoreId(category, storeid)
            );

        } else {

            response.put(
                    "product",
                    productRepository.findByNameAndCategory(
                            storeid,
                            name,
                            category
                    )
            );
        }

        return response;
    }

    @GetMapping("/search/{name}/{storeId}")
    public Map<String, Object> searchProduct(
            @PathVariable String name,
            @PathVariable long storeId) {

        Map<String, Object> response = new HashMap<>();

        response.put(
                "product",
                productRepository.findByNameLike(storeId, name)
        );

        return response;
    }

    @DeleteMapping("/{id}")
    public Map<String, String> removeProduct(@PathVariable long id) {

        Map<String, String> response = new HashMap<>();

        if (!serviceClass.ValidateProductId(id)) {
            response.put("message", "Product not present in database");
            return response;
        }

        inventoryRepository.deleteByProductId(id);
        response.put("message", "Product deleted successfully");

        return response;
    }

    @GetMapping("/validate/{quantity}/{storeId}/{productId}")
    public boolean validateQuantity(
            @PathVariable int quantity,
            @PathVariable long storeId,
            @PathVariable long productId) {

        Inventory inventory = inventoryRepository
                .findByProductIdandStoreId(productId, storeId);

        return inventory != null
                && inventory.getStockLevel() >= quantity;
    }
}