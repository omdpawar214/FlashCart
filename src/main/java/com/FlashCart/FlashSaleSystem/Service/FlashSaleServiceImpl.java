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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    @Override
    public FlashSaleDTO getSaleById(Long saleId) {
        //fetch the sale byId
        FlashSale sale = flashSaleRepository.findById(saleId).orElseThrow(()->
                new ResourceNotFoundException("FLashSale","flashSaleID",saleId));
        //converting to the DTO
        FlashSaleDTO flashSaleDTO = modelMapper.map(sale, FlashSaleDTO.class);
        //returning result
        return flashSaleDTO;
    }

    @Override
    public List<FlashSaleDTO> getAllSales() {
        //fetch all sales
        List<FlashSale> flashSales = flashSaleRepository.findAll();
        //converting sales into DTOs
        List<FlashSaleDTO> flashSaleDTOS = new ArrayList<>();
        for (FlashSale sale:flashSales){
            flashSaleDTOS.add(modelMapper.map(sale, FlashSaleDTO.class));
        }

        return flashSaleDTOS;
    }

    @Override
    public List<FlashSaleDTO> getAllActiveSales() {
        //get all the FlashSales from the repository
        List<FlashSale> flashSales = flashSaleRepository.findAll();
        //separate active sales from the list of sales
        List<FlashSale> ActiveFlashSales = new ArrayList<>();
        for (FlashSale sale:flashSales){
            if(sale.getEndsAt().isAfter(LocalDateTime.now())){
                ActiveFlashSales.add(sale);
            }
        }
        if (ActiveFlashSales.isEmpty()) throw new APIException("Currently No Active Sales");
        //converting Active sales TO DTOs
        List<FlashSaleDTO> activeFlashSaleDTOS = new ArrayList<>();
        for (FlashSale sale:ActiveFlashSales){
            activeFlashSaleDTOS.add(modelMapper.map(sale, FlashSaleDTO.class));
        }
        //return the separated list
        return activeFlashSaleDTOS;
    }
}
