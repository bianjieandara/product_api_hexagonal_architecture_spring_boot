package template.domain.ports.drivens;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import template.domain.models.Product;

/** Interface for querying Product entities. */
public interface ForQueryingProducts {

    /**
     * Update a product.
     *
     * @param id             the ID of the entity
     * @param updatedProduct the updated entity
     * @return the updated entity
     */
    Mono<Product> updateProduct(Long id, Product updatedProduct);

    /**
     * Save a product.
     *
     * @param product the entity to save
     * @return the persisted entity
     */
    Mono<Product> saveProduct(Product product);

    /**
     * Delete the product by ID.
     *
     * @param id the ID of the entity
     */
    Mono<Void> deleteProduct(Long id);

    /**
     * Get one product by ID.
     *
     * @param id the ID of the entity
     * @return the entity
     */
    Mono<Product> getProductById(Long id);

    /**
     * Get all the products.
     *
     * @return the list of entities
     */
    Flux<Product> getAllProducts();
}
