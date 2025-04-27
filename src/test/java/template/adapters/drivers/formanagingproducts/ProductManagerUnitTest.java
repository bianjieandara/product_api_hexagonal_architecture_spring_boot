package template.adapters.drivers.formanagingproducts;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import template.adapters.drivens.ProductSpring;
import template.adapters.drivens.forqueryingproducts.ProductQuerier;
import template.domain.models.Product;
import template.domain.repositories.ProductRepository;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Unit tests for the ProductManager class. */
@SpringBootTest
public class ProductManagerUnitTest {

    private static final ProductRepository productRepository = mock(ProductRepository.class);
    private static final ProductQuerier productQuerier = new ProductQuerier(productRepository);
    private static final ProductManager productManager = new ProductManager(productQuerier);
    private static final Product productMock = new Product(1L, "Dummy Name", 100, 1);
    private static final Long invalidProductId = -1L;
    private static final ProductSpring dummyProduct_1 = new ProductSpring(1L, "Dummy Name 1", 200, 5);
    private static final ProductSpring dummyProduct_2 = new ProductSpring(2L, "Dummy Name 2", 600, 3);

    @Test
    void deleteProductInvalidId() {
        assertThrows(IllegalArgumentException.class, () -> productManager.deleteProduct(invalidProductId));
    }

    @Test
    void saveProductInvalidName() {
        String invalidName = "invalidName invalidName invalidName";
        Product product = new Product(productMock.getId(), invalidName, productMock.getPrice(), productMock.getQuantity());
        assertThrows(IllegalArgumentException.class, () -> productManager.saveProduct(product));
    }

    @Test
    void saveProductInvalidPrice() {
        double invalidPrice = -1.0;
        Product product = new Product(productMock.getId(), productMock.getName(), invalidPrice, productMock.getQuantity());
        assertThrows(IllegalArgumentException.class, () -> productManager.saveProduct(product));
    }

    @Test
    void saveProductInvalidQuantity() {
        int invalidQuantity = -1;
        Product product = new Product(productMock.getId(), productMock.getName(), productMock.getPrice(), invalidQuantity);
        assertThrows(IllegalArgumentException.class, () -> productManager.saveProduct(product));
    }

    @Test
    void testGetAllProducts() {
        Flux<ProductSpring> productSpringList = Flux.fromIterable(List.of(dummyProduct_1, dummyProduct_2));
        when(productRepository.findAll()).thenReturn(productSpringList);
        StepVerifier.create(productManager.getAllProducts())
            .expectNextCount(2)
            .expectComplete()
            .verify();
    }
}
