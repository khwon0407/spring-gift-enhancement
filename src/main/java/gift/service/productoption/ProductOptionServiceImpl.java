package gift.service.productoption;

import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import gift.entity.Product;
import gift.entity.ProductOption;
import gift.exception.notfound.NoProductInfoException;
import gift.repository.product.ProductRepositoryJpa;
import gift.repository.productoption.ProductOptionRepositoryJpa;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;

@Service
public class ProductOptionServiceImpl implements ProductOptionService {
    private final ProductRepositoryJpa productRepositoryJpa;
    private final ProductOptionRepositoryJpa productOptionRepositoryJpa;
    
    public ProductOptionServiceImpl(ProductRepositoryJpa productRepositoryJpa,
        ProductOptionRepositoryJpa productOptionRepositoryJpa) {
        this.productRepositoryJpa = productRepositoryJpa;
        this.productOptionRepositoryJpa = productOptionRepositoryJpa;
    }
    
    @Override
    public List<OptionResponseDto> findProductOptionsById(Long id) {
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        return product.getOptions().stream().map(option -> new OptionResponseDto(
            option.getId(),
            option.getName(),
            option.getQuantity()
        )).collect(Collectors.toList());
    }
    
    @Override
    public OptionResponseDto addOptionsToProduct(Long id, OptionRequestDto optionRequestDto) {
        Product product = productRepositoryJpa.findById(id).orElseThrow(NoProductInfoException::new);
        ProductOption option = new ProductOption(null, optionRequestDto.name(), optionRequestDto.quantity(), product);
        product.addOptions(option);
        productRepositoryJpa.save(product);
        
        ProductOption savedOption = product.getOptions().get(product.getOptions().size() - 1);
        
        return new OptionResponseDto(
            savedOption.getId(),
            savedOption.getName(),
            savedOption.getQuantity()
        );
    }
}
