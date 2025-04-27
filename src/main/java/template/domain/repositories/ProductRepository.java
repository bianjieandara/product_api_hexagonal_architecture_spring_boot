package template.domain.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import template.adapters.drivens.ProductSpring;

/** Repository interface for the Product model. */
@Repository
public interface ProductRepository extends ReactiveCrudRepository<ProductSpring, Long> {}
