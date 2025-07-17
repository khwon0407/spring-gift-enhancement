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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ProductServiceImpl implements ProductService {
    
    private final ProductRepositoryJpa productRepositoryJpa;
    
    public ProductServiceImpl(ProductRepositoryJpa productRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
    }
    
    //상품 추가 Service
    @Override
    @Transactional
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
    public List<ProductResponseDto> findAllProducts(int pageNo, int pageSize, String criteria) {
        Pageable pageable = PageRequest.of(pageNo, pageSize, Sort.by(Direction.ASC, criteria));
        Page<ProductResponseDto> page =  productRepositoryJpa.findAll(pageable).map(ProductResponseDto::new);
        return page.getContent();
    }
    
    //상품 단건 조회
    @Override
    public ProductResponseDto findProductWithId(Long id) {
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        return new ProductResponseDto(product);
    }
    
    //상품 수정 (상품 자체가 다른 것으로 바뀜)
    @Override
    @Transactional
    public ProductResponseDto modifyProductWithId(Long id,
        ModifyProductRequestDto requestDto) {
        
        if (requestDto.isNotValidForModify()) {
            throw new FillAllInfoException();
        }
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        
        product.changeProductInfo(requestDto.name(), requestDto.price(), requestDto.imageUrl());
        
        return new ProductResponseDto(productRepositoryJpa.save(product));
    }
    
    //상품 단건 삭제
    @Override
    @Transactional
    public void deleteProductWithId(Long id) {
        productRepositoryJpa.deleteById(id);
    }
    
    //상품 수정 (일부 내용이 바뀜)
    @Override
    @Transactional
    public ProductResponseDto modifyProductInfoWithId(Long id,
        ModifyProductRequestDto requestDto) {
        
        if (requestDto.isNotValidForModifyInfo()) {
            throw new FillSomeInfoException();
        }
        
        if(!requestDto.goodName()) {
            throw new CheckMdOkException();
        }
        
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        
        product.changeProductInfo(
            requestDto.name() != null ? requestDto.name() : product.getName(),
            requestDto.price() != null ? requestDto.price() : product.getPrice(),
            requestDto.imageUrl() != null ? requestDto.imageUrl() : product.getImageUrl()
        );
        
        return new ProductResponseDto(productRepositoryJpa.save(product));
    }
    
}
