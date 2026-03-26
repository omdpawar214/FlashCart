package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.FlashSaleDTO;
import com.FlashCart.FlashSaleSystem.ErrorControl.APIException;
import com.FlashCart.FlashSaleSystem.ErrorControl.ResourceNotFoundException;
import com.FlashCart.FlashSaleSystem.Models.FlashSale;
import com.FlashCart.FlashSaleSystem.Models.Product;
import com.FlashCart.FlashSaleSystem.Repository.FlashSaleRepository;
import com.FlashCart.FlashSaleSystem.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
public class FlashSaleServiceImpl implements FlashSaleService{
    private final ModelMapper modelMapper;
    private final FlashSaleRepository flashSaleRepository;
    private final ProductRepository productRepository;

    public FlashSaleServiceImpl(ModelMapper modelMapper, FlashSaleRepository flashSaleRepository, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.flashSaleRepository = flashSaleRepository;
        this.productRepository = productRepository;
    }

    @Override
    @Transactional
    public FlashSaleDTO createSale(FlashSaleDTO flashSaleDTO) {

        //validating sale according to stock
        Integer saleStock = flashSaleDTO.getSaleStock();
        Long productId = flashSaleDTO.getProductId();
        if (productId==null) throw new APIException("productId must not be null");

        Product product = productRepository.findById(productId).orElseThrow(()->
                new ResourceNotFoundException("Product","Product_id",productId));

        Integer productStock = product.getStock();
        if (saleStock>productStock){
            throw new APIException("Product Out of Stock!!!");
        }
        //converting dto to model
        FlashSale flashSale = modelMapper.map(flashSaleDTO, FlashSale.class);
        flashSale.setProduct(product);
        //saving the sale in repository
        FlashSale dbSale = flashSaleRepository
                .findByProduct_ProductIdAndStartAtAndEndsAt(productId,
                        flashSaleDTO.getStartAt(),
                        flashSaleDTO.getEndsAt());

        if (dbSale!=null) throw new APIException("Sale Already Exists");
        FlashSale savedSale = flashSaleRepository.save(flashSale);
        //returning the dto of saved model
        return modelMapper.map(savedSale, FlashSaleDTO.class);
    }
}
