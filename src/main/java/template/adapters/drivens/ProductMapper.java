package template.adapters.drivens;

import reactor.core.publisher.Flux;
import template.domain.models.Product;

/** Maps a model product to a Spring product and vice versa. */
public final class ProductMapper {

    private ProductMapper() {}

    public static ProductSpring toSpringEntity(Product product) {
        return new ProductSpring(product.getName(), product.getPrice(), product.getQuantity());
    }

    public static Product toModelEntity(ProductSpring productSpring) {
        return new template.domain.models.Product(
                productSpring.getId(),
                productSpring.getName(),
                productSpring.getPrice(),
                productSpring.getQuantity()
        );
    }

    public static Flux<Product> toModelEntities(Flux<ProductSpring> productSpringFlux) {
        return productSpringFlux.map(ProductMapper::toModelEntity);
    }
}
