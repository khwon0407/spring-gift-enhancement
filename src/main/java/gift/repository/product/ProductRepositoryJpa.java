package gift.repository.product;

import gift.entity.Product;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepositoryJpa extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);
}
