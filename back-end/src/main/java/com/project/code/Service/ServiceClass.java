package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;

import org.springframework.stereotype.Service;

@Service
public class ServiceClass {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ServiceClass(
            ProductRepository productRepository,
            InventoryRepository inventoryRepository) {

        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    public boolean validateInventory(Inventory inventory) {

        Inventory existingInventory = inventoryRepository
                .findByProductIdandStoreId(
                        inventory.getProduct().getId(),
                        inventory.getStore().getId()
                );

        return existingInventory == null;
    }

    public boolean validateProduct(Product product) {

        Product existingProduct = productRepository
                .findByName(product.getName());

        return existingProduct == null;
    }

    public boolean ValidateProductId(long id) {
        Product product = productRepository.findByid(id);

        return product != null;
    }

    public Inventory getInventoryId(Inventory inventory) {

        return inventoryRepository.findByProductIdandStoreId(
                inventory.getProduct().getId(),
                inventory.getStore().getId()
        );
    }
}
