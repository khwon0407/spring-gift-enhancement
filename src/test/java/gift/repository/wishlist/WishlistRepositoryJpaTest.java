package gift.repository.wishlist;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import gift.entity.Member;
import gift.entity.Product;
import gift.entity.Role;
import gift.entity.WishlistInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class WishlistRepositoryJpaTest {
    @Autowired
    private WishlistRepositoryJpa wishlistRepositoryJpa;
    
    @Test
    void 저장() {
        WishlistInfo wishlistInfo = new WishlistInfo(
            null,
            new Member(2L, "user@user.com", "userpw", Role.USER),
            new Product(1L, "아메리카노", 4500L, "http://image.url.americano"),
            5L
        );
        
        var actual = wishlistRepositoryJpa.save(wishlistInfo);
        
        assertAll(
            () -> assertThat(actual.getId()).isNotNull(),
            () -> assertThat(actual.getProductCnt()).isEqualTo(wishlistInfo.getProductCnt())
        );
    }
    
    @Test
    void 검색() {
        WishlistInfo wishlistInfo = new WishlistInfo(
            null,
            new Member(2L, "user@user.com", "userpw", Role.USER),
            new Product(1L, "아메리카노", 4500L, "http://image.url.americano"),
            5L
        );
        
        wishlistRepositoryJpa.save(wishlistInfo);
        
        var actual = wishlistRepositoryJpa.findAllByMemberId(2L).get(0).getProductCnt();
        
        assertThat(actual).isEqualTo(wishlistInfo.getProductCnt());
    }
}