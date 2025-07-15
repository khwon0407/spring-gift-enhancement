package gift.repository.wishlist;

import gift.entity.WishlistInfo;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepositoryJpa extends JpaRepository<WishlistInfo, Long> {
    Optional<WishlistInfo> findByMemberIdAndProductId(Long memberId, Long productId);
    
    List<WishlistInfo> findAllByMemberId(Long memberId);
    
    void deleteByMemberIdAndProductId(Long memberId, Long productId);
}
