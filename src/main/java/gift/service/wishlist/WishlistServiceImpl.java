package gift.service.wishlist;

import gift.dto.api.wishlist.WishlistRequestDto;
import gift.dto.api.wishlist.WishlistResponseDto;
import gift.entity.Member;
import gift.entity.Product;
import gift.entity.WishlistInfo;
import gift.exception.notfound.NoProductInfoException;
import gift.exception.notfound.NotInWishlistException;
import gift.exception.unauthorized.WrongIdOrPasswordException;
import gift.repository.member.MemberRepositoryJpa;
import gift.repository.product.ProductRepositoryJpa;
import gift.repository.wishlist.WishlistRepositoryJpa;
import java.util.ArrayList;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class WishlistServiceImpl implements WishlistService {
    
    private final ProductRepositoryJpa productRepositoryJpa;
    private final WishlistRepositoryJpa wishlistRepositoryJpa;
    
    public WishlistServiceImpl(ProductRepositoryJpa productRepositoryJpa,
        WishlistRepositoryJpa wishlistRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
        this.wishlistRepositoryJpa = wishlistRepositoryJpa;
    }
    
    @Override
    @Transactional
    public WishlistResponseDto addToMyWishlist(Member user, WishlistRequestDto requestDto) {
        
        Product product = productRepositoryJpa.findById(requestDto.productId()).orElseThrow(
            NoProductInfoException::new);
        
        WishlistInfo saved = wishlistRepositoryJpa.save(
            new WishlistInfo(null, user, product, requestDto.productCnt())
        );
        
        return new WishlistResponseDto(saved);
    }
    
    @Override
    public List<WishlistResponseDto> findMyWishlist(Member user, int pageNo, int pageSize, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.ASC, criteria));
        Page<WishlistResponseDto> page = wishlistRepositoryJpa.findAllByMemberId(user.getId(), pageable)
            .map(wishlist -> new WishlistResponseDto(
                wishlist.getProduct().getId(),
                wishlist.getProduct().getName(),
                wishlist.getProductCnt()
            ));
        return page.getContent();
    }
    
    @Override
    @Transactional
    public void deleteFromMyWishlist(Member user, Long productId) {
        Product product = productRepositoryJpa.findById(productId).orElseThrow(NoProductInfoException::new);
        
        WishlistInfo wishlistInfo = wishlistRepositoryJpa.findByMemberIdAndProductId(user.getId(), product.getId()).orElseThrow(
            NotInWishlistException::new);
        
        wishlistRepositoryJpa.deleteByMemberIdAndProductId(wishlistInfo.getMember().getId(), wishlistInfo.getProduct().getId());
    }
    
    @Override
    @Transactional
    public WishlistResponseDto modifyProductCntFromMyWishlist(Member user,
        WishlistRequestDto requestDto) {
        Product product = productRepositoryJpa.findById(requestDto.productId())
            .orElseThrow(NoProductInfoException::new);
        
        WishlistInfo wishlistInfo = wishlistRepositoryJpa.findByMemberIdAndProductId(user.getId(), product.getId())
            .orElseThrow(NotInWishlistException::new);
        
        if(requestDto.productCnt() == 0) {
            deleteFromMyWishlist(wishlistInfo.getMember(), wishlistInfo.getProduct().getId());
            return null;
        }
        
        wishlistInfo.changeProductCnt(requestDto.productCnt());
        
        return new WishlistResponseDto(wishlistRepositoryJpa.save(wishlistInfo));
    }
}
