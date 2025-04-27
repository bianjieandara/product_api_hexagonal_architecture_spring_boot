package template.adapters.drivers.formanagingproducts;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import template.adapters.drivens.ProductSpring;
import template.domain.models.Product;
import template.domain.repositories.ProductRepository;

import java.util.ArrayList;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/** Integration tests for the ProductManager class. */
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ProductManagerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final ProductRepository productRepository = mock(ProductRepository.class);
    private static final Product newProduct = new Product("New Product", 50, 10);
    private static final Product updatedProduct = new Product(1L,"Product Updated", 500, 10);
    private static final ProductSpring dummyProductSpring = new ProductSpring(1L, "Dummy Name 1", 200, 5);

    @Test
    public void testSaveProduct() {
        webTestClient.post()
            .uri("/api/v1/product")
            .body(Mono.just(newProduct), Product.class)
            .exchange()
            .expectStatus().isOk()
            .expectBody(Product.class)
            .consumeWith(response -> {
                Product createdItem = response.getResponseBody();
                Assertions.assertNotNull(createdItem);
                assertEquals(createdItem.getName(), newProduct.getName());
                assertEquals(createdItem.getPrice(), newProduct.getPrice());
                assertEquals(createdItem.getQuantity(), newProduct.getQuantity());
            });
    }

    @Test
    void testGetProductById() {
        webTestClient.get().uri("/api/v1/products/1")
            .accept(MediaType.APPLICATION_JSON)
            .exchange()
            .expectStatus().isOk()
            .expectBody()
            .jsonPath("$.id").isEqualTo(1);
    }

    @Test
    public void testUpdateProduct() {
        Long dummyProductId = 1L;
        when(productRepository.findById(dummyProductId)).thenReturn(Mono.just(dummyProductSpring));
        webTestClient.put().uri("/api/v1/products/1").contentType(MediaType.APPLICATION_JSON)
            .bodyValue(updatedProduct).exchange().expectStatus().isOk().expectBody(Product.class)
            .isEqualTo(updatedProduct);
    }

    @Test
    public void testGetAllProducts() {
        when(productRepository.findAll()).thenReturn(Flux.just(dummyProductSpring));
        webTestClient.get().uri("/api/v1/products").accept(MediaType.APPLICATION_JSON)
            .exchange().expectStatus().isOk().expectBody(ArrayList.class)
            .consumeWith(response -> {
                Assertions.assertNotNull(response.getResponseBody());
                JSONObject jsonObject = new JSONObject((Map) response.getResponseBody().getFirst());
                try {
                    assertEquals(jsonObject.getString("name"), updatedProduct.getName());
                    assertEquals(jsonObject.getDouble("price"), updatedProduct.getPrice());
                    assertEquals(jsonObject.getInt("quantity"), updatedProduct.getQuantity());
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}
