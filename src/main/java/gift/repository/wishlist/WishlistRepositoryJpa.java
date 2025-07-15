package gift.repository.wishlist;

import gift.entity.WishlistInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepositoryJpa extends JpaRepository<WishlistInfo, Long> {

}
