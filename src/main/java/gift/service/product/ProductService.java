package gift.service.product;

import gift.dto.api.product.AddProductRequestDto;
import gift.dto.api.product.ModifyProductRequestDto;
import gift.dto.api.product.ProductResponseDto;
import org.springframework.data.domain.Page;

public interface ProductService {
    
    ProductResponseDto addProduct(AddProductRequestDto requestDto);
    
    Page<ProductResponseDto> findAllProducts(int pageNo, int pageSize, String criteria);
    
    ProductResponseDto findProductWithId(Long id);
    
    ProductResponseDto modifyProductWithId(Long id, ModifyProductRequestDto requestDto);
    
    void deleteProductWithId(Long id);
    
    ProductResponseDto modifyProductInfoWithId(Long id, ModifyProductRequestDto requestDto);
}
