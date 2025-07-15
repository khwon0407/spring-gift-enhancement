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
import org.springframework.stereotype.Service;

@Service
public class WishlistServiceImpl implements WishlistService {
    
    /*
    private final WishlistRepository wishlistRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    */
    private final ProductRepositoryJpa productRepositoryJpa;
    private final WishlistRepositoryJpa wishlistRepositoryJpa;
    
    public WishlistServiceImpl(ProductRepositoryJpa productRepositoryJpa,
        WishlistRepositoryJpa wishlistRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
        this.wishlistRepositoryJpa = wishlistRepositoryJpa;
    }
    
    /*
    public WishlistServiceImpl(WishlistRepository wishlistRepository,
        ProductRepository productRepository, MemberRepository memberRepository) {
        this.wishlistRepository = wishlistRepository;
        this.productRepository = productRepository;
        this.memberRepository = memberRepository;
    }
    */
    
    @Override
    public WishlistResponseDto addToMyWishlist(Member user, WishlistRequestDto requestDto) {
        
        Product product = productRepositoryJpa.findById(requestDto.productId()).orElseThrow(
            NoProductInfoException::new);
        
        WishlistInfo saved = wishlistRepositoryJpa.save(
            new WishlistInfo(null, user, product, requestDto.productCnt())
        );
        
        return new WishlistResponseDto(saved.getId(), saved.getProduct().getName(),
            saved.getProductCnt());
    }
    
    @Override
    public List<WishlistResponseDto> findMyWishlist(Member user) {
        List<WishlistInfo> myWishlist = wishlistRepositoryJpa.findAllByMemberId(user.getId());
        
        List<WishlistResponseDto> responseDtoList = new ArrayList<>();
        for (WishlistInfo info : myWishlist) {
            WishlistResponseDto dto = new WishlistResponseDto(
                info.getId(),
                info.getProduct().getName(),
                info.getProductCnt()
            );
            responseDtoList.add(dto);
        }
        
        return responseDtoList;
    }
    
    @Override
    public void deleteFromMyWishlist(Member user, Long productId) {
        Product product = productRepositoryJpa.findById(productId).orElseThrow(NoProductInfoException::new);
        
        WishlistInfo wishlistInfo = wishlistRepositoryJpa.findByMemberIdAndProductId(user.getId(), product.getId()).orElseThrow(
            NotInWishlistException::new);
        
        wishlistRepositoryJpa.deleteByMemberIdAndProductId(wishlistInfo.getMember().getId(), wishlistInfo.getProduct().getId());
    }
    
    @Override
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
        
        wishlistInfo.setProductCnt(requestDto.productCnt());
        
        return new WishlistResponseDto(wishlistRepositoryJpa.save(wishlistInfo));
    }
}
