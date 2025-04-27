package template.domain.services;

import template.domain.models.Product;

/** Validates product attributes. */
public class Validation {

    public static void validateId(Long id) {
        if (id < 1) {
            throw new IllegalArgumentException("'Id' must be a positive integer");
        }
    }

    public static void validateProduct(Product product) {
        if (product.getPrice() < 0) {
            throw new IllegalArgumentException("Price must be a zero or a positive number");
        }
        if (product.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity must be a zero or a positive number");
        }
        if (product.getName().length() > 25) {
            throw new IllegalArgumentException("Name must not exceed 25 characters");
        }
    }
}
