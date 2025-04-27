package template.adapters.drivens.forqueryingproducts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import template.adapters.drivens.ProductMapper;
import template.adapters.drivens.ProductSpring;
import template.domain.models.Product;
import template.domain.repositories.ProductRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Unit tests for the ProductQuerier class. */
@SpringBootTest
public class ProductQuerierTest {

    private static final ProductRepository productRepository = mock(ProductRepository.class);
    private static final ProductQuerier productQuerier = new ProductQuerier(productRepository);
    private static final Product productMock = new Product(1L, "Dummy Name", 100, 1);
    private static final ProductSpring dummyProduct_1 = new ProductSpring(1L, "Dummy Name 1", 200, 5);
    private static final ProductSpring dummyProduct_2 = new ProductSpring(2L, "Dummy Name 2", 600, 3);
    private static final Long dummyProductId = 1L;

    @Test
    void saveProduct() {
        ProductSpring productSpring = ProductMapper.toSpringEntity(productMock);
        when(productRepository.save(productSpring)).thenReturn(Mono.just(productSpring));
        Mono<Product> product = productQuerier.saveProduct(productMock);
        product.doOnSuccess((item) -> assertEquals(productMock, item));
    }

    @Test
    public void testUpdateProduct() {
        when(productRepository.findById(dummyProductId)).thenReturn(Mono.just(dummyProduct_1));
        Mono<Product> product = productQuerier.updateProduct(dummyProductId, productMock);
        product.doOnSuccess((item) -> assertEquals(productMock, item));
    }

    @Test
    void testGetProductById() {
        when(productRepository.findById(dummyProductId)).thenReturn(Mono.just(dummyProduct_1));
        Mono<Product> product = productQuerier.getProductById(dummyProductId);
        product.doOnSuccess((item) -> assertEquals(ProductMapper.toModelEntity(dummyProduct_1), item));
    }

    @Test
    void testGetAllProducts() {
        Flux<ProductSpring> productSpringList = Flux.fromIterable(List.of(dummyProduct_1, dummyProduct_2));
        when(productRepository.findAll()).thenReturn(productSpringList);
        StepVerifier.create(productQuerier.getAllProducts())
            .expectNextCount(2)
            .expectComplete()
            .verify();
    }
}
