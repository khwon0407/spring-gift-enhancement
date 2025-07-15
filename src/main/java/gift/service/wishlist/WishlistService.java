package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.Member;
import java.util.List;

public interface WishlistService {
    
    WishlistResponseDto addToMyWishlist(Member user, WishlistRequestDto requestDto);
    
    List<WishlistResponseDto> findMyWishlist(Member user);
    
    void deleteFromMyWishlist(Member user, Long productId);
    
    WishlistResponseDto modifyProductCntFromMyWishlist(Member user, WishlistRequestDto requestDto);
}
