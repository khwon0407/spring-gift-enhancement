package gift.service.productoption;

import gift.dto.api.product.OptionRequestDto;
import gift.dto.api.product.OptionResponseDto;
import gift.entity.Product;
import gift.entity.ProductOption;
import gift.exception.badrequest.LessQuantityException;
import gift.exception.badrequest.WrongProductOptionException;
import gift.exception.notfound.NoOptionInfoException;
import gift.exception.notfound.NoProductInfoException;
import gift.repository.product.ProductRepositoryJpa;
import gift.repository.productoption.ProductOptionRepositoryJpa;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
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
    
    @Override
    @Transactional
    public void deleteOptionToProduct(Long productId, Long optionId) {
        Product product = productRepositoryJpa.findById(productId).orElseThrow(NoProductInfoException::new);
        ProductOption option = productOptionRepositoryJpa.findById(optionId).orElseThrow(NoOptionInfoException::new);
        
        if(!product.getOptions().contains(option) || !option.getProduct().equals(product)) {
            throw new WrongProductOptionException();
        }
        
        product.removeOptions(option);
        
        productOptionRepositoryJpa.deleteById(optionId);
    }
    
    @Override
    @Transactional
    public OptionResponseDto modifyOptionsToProduct(Long productId, Long optionId,
        OptionRequestDto optionRequestDto) {
        Product product = productRepositoryJpa.findById(productId).orElseThrow(NoProductInfoException::new);
        ProductOption option = productOptionRepositoryJpa.findById(optionId).orElseThrow(NoOptionInfoException::new);
        
        if(!product.getOptions().contains(option) || !option.getProduct().equals(product)) {
            throw new WrongProductOptionException();
        }
        
        option.changeInfo(optionRequestDto.name(), optionRequestDto.quantity());
        ProductOption saved = productOptionRepositoryJpa.save(option);
        
        return new OptionResponseDto(
            saved.getId(),
            saved.getName(),
            saved.getQuantity()
        );
    }
    
    @Override
    @Transactional
    public void decreaseOptionQuantity(Long optionId, Long quantity) {
        ProductOption option = productOptionRepositoryJpa.findById(optionId)
            .orElseThrow(NoOptionInfoException::new);
        
        Long currentQuantity = option.getQuantity();
        if (currentQuantity == null || currentQuantity < quantity) {
            throw new LessQuantityException();
        }
        
        option.decreaseQuantity(quantity);
        
        productOptionRepositoryJpa.save(option);
    }
}
