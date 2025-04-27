package template.adapters.drivers.formanagingproducts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import template.adapters.drivens.forqueryingproducts.ProductQuerier;
import template.domain.models.Product;
import template.domain.ports.drivers.ForManagingProducts;
import template.domain.services.Validation;

/** Controller class for managing product entities.*/
@RestController
@RequestMapping("/api/v1")
public class ProductManager implements ForManagingProducts {

    private final ProductQuerier productQuerier;

    @Autowired
    public ProductManager(ProductQuerier productQuerier) {
        this.productQuerier = productQuerier;
    }

    @PostMapping("/product")
    public Mono<Product> saveProduct(@RequestBody Product product) {
        Validation.validateProduct(product);
        Mono<Product> productSaved = this.productQuerier.saveProduct(product);
        return productSaved.doOnSuccess(ResponseEntity::ok);
    }

    @GetMapping("/products")
    public Flux<Product> getAllProducts() {
        return this.productQuerier.getAllProducts();
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Validation.validateId(id);
        Mono<Product> product = this.productQuerier.getProductById(id);
        return product.map(ResponseEntity::ok).blockOptional().orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/products/{id}")
    public Mono<ResponseEntity<Product>> updateProduct(@PathVariable Long id, @RequestBody Product product) {
        Validation.validateId(id);
        Validation.validateProduct(product);
        return this.productQuerier.updateProduct(id, product).map(ResponseEntity::ok).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/products/{id}")
    public Mono<Void> deleteProduct(@PathVariable Long id) {
        Validation.validateId(id);
        return this.productQuerier.deleteProduct(id).doOnSuccess(ResponseEntity::ok);
    }
}
