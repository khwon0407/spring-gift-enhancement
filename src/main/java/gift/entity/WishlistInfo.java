package gift.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "wishlist")
public class WishlistInfo {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "memberId", nullable = false, foreignKey = @ForeignKey(name = "fk_wishlist_member"))
    private Member member;
    
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "productId", nullable = false, foreignKey = @ForeignKey(name = "fk_wishlist_product"))
    private Product product;
    
    @Column(name = "productCnt", nullable = false)
    private Long productCnt;
    
    public WishlistInfo(Long id, Member member, Product product, Long productCnt) {
        Id = id;
        this.member = member;
        this.product = product;
        this.productCnt = productCnt;
    }
    
    public WishlistInfo() {
    
    }
    
    public Member getMember() {
        return member;
    }
    
    public Product getProduct() {
        return product;
    }
    
    public Long getProductCnt() {
        return productCnt;
    }
}
