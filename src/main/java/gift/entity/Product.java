package gift.entity;

import gift.exception.badrequest.WrongPriceException;
import gift.exception.badrequest.WrongProductCntException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.util.List;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "name", nullable = false)
    private String name;
    
    @Column(name = "price", nullable = false)
    private Long price;
    
    @Column(name = "image_url", nullable = false)
    private String imageUrl;
    
    @OneToMany(mappedBy = "options", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Option> options;
    
    @PrePersist
    @PreUpdate
    private void validatePrice() {
        if (price == null || price < 0) {
            throw new WrongPriceException();
        }
    }
    
    public Product(Long id, String name, Long price, String imageUrl) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    
    public Product() {
    
    }
    
    public Long getId() {
        return id;
    }
    
    public String getName() {
        return name;
    }
    
    public Long getPrice() {
        return price;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public List<Option> getOptions() {
        return options;
    }
    
    public void changeProductInfo(String name, Long price, String imageUrl) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
    }
    
    public void addOptions(Option option) {
        this.options.add(option);
        option.setProduct(this);
    }
}
