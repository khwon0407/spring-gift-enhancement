package gift.service.product;

import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.ProductResponseDto;
import gift.entity.Product;
import gift.exception.badrequest.CheckMdOkException;
import gift.exception.badrequest.FillAllInfoException;
import gift.exception.badrequest.FillSomeInfoException;
import gift.exception.notfound.NoProductInfoException;
import gift.repository.product.ProductRepositoryJpa;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    
    //private final ProductRepository productRepository;
    private final ProductRepositoryJpa productRepositoryJpa;
    
    public ProductServiceImpl(ProductRepositoryJpa productRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
    }
    
    /*
    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    */
    
    
    //상품 추가 Service
    @Override
    public ProductResponseDto addProduct(AddProductRequestDto requestDto) {
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = new Product(
            null,
            requestDto.name(),
            requestDto.price(),
            requestDto.imageUrl()
        );
        
        Product saved = productRepositoryJpa.save(product);
        
        return new ProductResponseDto(saved);
    }
    
    //상품 전체 조회
    @Override
    public List<ProductResponseDto> findAllProducts() {
        return productRepositoryJpa.findAll().stream()
            .map(ProductResponseDto::new)
            .collect(Collectors.toList());
    }
    
    //상품 단건 조회
    @Override
    public ProductResponseDto findProductWithId(Long id) {
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        return new ProductResponseDto(product);
    }
    
    //상품 수정 (상품 자체가 다른 것으로 바뀜)
    @Override
    public ProductResponseDto modifyProductWithId(Long id,
        ModifyProductRequestDto requestDto) {
        
        if (requestDto.isNotValidForModify()) {
            throw new FillAllInfoException();
        }
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        
        product.setName(requestDto.name());
        product.setPrice(requestDto.price());
        product.setImageUrl(requestDto.imageUrl());
        
        return new ProductResponseDto(productRepositoryJpa.save(product));
    }
    
    //상품 단건 삭제
    @Override
    public void deleteProductWithId(Long id) {
        productRepositoryJpa.deleteById(id);
    }
    
    //상품 수정 (일부 내용이 바뀜)
    @Override
    public ProductResponseDto modifyProductInfoWithId(Long id,
        ModifyProductRequestDto requestDto) {
        
        if (requestDto.isNotValidForModifyInfo()) {
            throw new FillSomeInfoException();
        }
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        
        product.setName(requestDto.name() != null ? requestDto.name() : product.getName());
        product.setPrice(requestDto.price() != null ? requestDto.price() : product.getPrice());
        product.setImageUrl(requestDto.imageUrl() != null ? requestDto.imageUrl() : product.getImageUrl());
        
        return new ProductResponseDto(productRepositoryJpa.save(product));
    }
    
}
