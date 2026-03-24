package com.FlashCart.FlashSaleSystem.Service;

import com.FlashCart.FlashSaleSystem.DTOs.ProductDTO;
import com.FlashCart.FlashSaleSystem.Models.Product;
import com.FlashCart.FlashSaleSystem.Repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService{
    private ProductRepository productRepository;
    private ModelMapper modelMapper;
    public ProductServiceImpl(ModelMapper modelMapper, ProductRepository productRepository) {
        this.modelMapper = modelMapper;
        this.productRepository = productRepository;
    }

    @Override
    public List<ProductDTO> getAllProducts() {
        //fetching all the products from repository
        List<Product> products = productRepository.findAll();
        //converting products to productDTO
        List<ProductDTO> productDTOS = new ArrayList<>();
        for (Product product:products){
            productDTOS.add(modelMapper.map(product, ProductDTO.class));
        }
        //returning the result
        return productDTOS;
    }

    @Transactional
    @Override
    public ProductDTO createProduct(ProductDTO productDTO) {
        //convert the dto tp product
        Product product  = modelMapper.map(productDTO, Product.class);
        //save the product to repository
        Product savedProduct = productRepository.save(product);
        //return the saved product
        return modelMapper.map(savedProduct, ProductDTO.class);
    }


    @Override
    @Transactional
    public String deleteProduct(Long productId) {
        //fetching the product from the repository
        Product product = productRepository.findById(productId).orElseThrow(()->
                new RuntimeException("Product Does not exist with ProductId: "+productId));
        //remove the product from the repository
        productRepository.delete(product);

        return "Product with productId:"+productId+" has been removed successfully";
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(ProductDTO productDTO) {
        //fetch the product using product id
        Long productId = productDTO.getProductId();
        Product product = productRepository.findById(productId).orElseThrow(()->  new RuntimeException("Product Does not exist with ProductId: "+productId));
        //set the updated fields
        product.setName(productDTO.getName());
        product.setDescription(productDTO.getDescription());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        //saving the updated product
        Product savedProduct = productRepository.save(product);

        return modelMapper.map(savedProduct, ProductDTO.class);
    }

    @Override
    public ProductDTO geProductById(Long productId) {
        Product product = productRepository.findById(productId).orElseThrow(()->
                new RuntimeException("Product Does not exist with ProductId: "+productId));
        return modelMapper.map(product, ProductDTO.class);
    }
}
