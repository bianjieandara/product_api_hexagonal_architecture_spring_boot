package template.adapters.drivens.forqueryingproducts;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import template.adapters.drivens.ProductSpring;
import template.adapters.drivens.ProductMapper;
import template.domain.models.Product;
import template.domain.repositories.ProductRepository;
import template.domain.ports.drivens.ForQueryingProducts;

/** Service class for querying product entities.*/
@Service
public class ProductQuerier implements ForQueryingProducts {

    private final ProductRepository productRepository;

    @Autowired
    public ProductQuerier(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Mono<Product> saveProduct(Product product) {
        ProductSpring productSpring = ProductMapper.toSpringEntity(product);
        return this.productRepository.save(productSpring).map(ProductMapper::toModelEntity);
    }

    @Override
    public Mono<Product> updateProduct(Long id, Product product) {
        return this.productRepository.findById(id)
                .flatMap(existingProduct -> {
                    existingProduct.setName(product.getName());
                    existingProduct.setQuantity(product.getQuantity());
                    existingProduct.setPrice(product.getPrice());
                    return this.productRepository.save(existingProduct);
                })
                .map(ProductMapper::toModelEntity);
    }

    @Override
    public Mono<Void> deleteProduct(Long id) {
        return this.productRepository.deleteById(id);
    }

    @Override
    public Mono<Product> getProductById(Long id) {
        return this.productRepository.findById(id).map(ProductMapper::toModelEntity);
    }

    @Override
    public Flux<Product> getAllProducts() {
        return ProductMapper.toModelEntities(this.productRepository.findAll());
    }
}
