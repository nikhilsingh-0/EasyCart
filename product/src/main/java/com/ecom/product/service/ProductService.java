package com.ecom.product.service;


import com.ecom.product.dto.*;
import com.ecom.product.model.Category;
import com.ecom.product.model.Product;
import com.ecom.product.repository.CategoryRepository;
import com.ecom.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;

    public ProductResponse createProduct(com.ecom.product.dto.ProductRequest productRequest) {
        Product product = new Product();
        updateProductFromRequest(product, productRequest);
        Product savedProduct = productRepository.save(product);
        return mapToProductResponse(savedProduct);
    }

    private ProductResponse mapToProductResponse(Product savedProduct) {
        ProductResponse response = new ProductResponse();
        response.setId(savedProduct.getId());
        response.setName(savedProduct.getName());
        response.setActive(savedProduct.getActive());
        response.setCategory(savedProduct.getCategory().getName());
        response.setDescription(savedProduct.getDescription());
        response.setPrice(savedProduct.getPrice());
        response.setImageUrl(savedProduct.getImageUrl());
        response.setStockQuantity(savedProduct.getStockQuantity());
        return response;
    }

    private void updateProductFromRequest(Product product, ProductRequest productRequest) {
        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setPrice(productRequest.getPrice());
        product.setSellerId(productRequest.getSellerId());
        product.setImageUrl(productRequest.getImageUrl());
        product.setStockQuantity(productRequest.getStockQuantity());

        Category category = categoryRepository.findById(productRequest.getCategory())
                .orElseThrow(() -> new RuntimeException(""));
        product.setCategory(category);
    }

    @CacheEvict(value = {"product", "products"}, key = "#id", allEntries = true)
    public Optional<ProductResponse> updateProduct(Long id, ProductRequest productRequest) {
        return productRepository.findById(id)
                .map(existingProduct -> {
                    updateProductFromRequest(existingProduct, productRequest);
                    Product savedProduct = productRepository.save(existingProduct);
                    return mapToProductResponse(savedProduct);
                });
    }

    @Cacheable(value = "products", key = "'page:' + #page + ':size:' + #size + ':sort:' + #sortBy + ':' + #sortDir")
    public PageResponse<ProductResponse> getProducts(int page, int size, String sortBy, String sortDir) {

        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Product> productPage = productRepository.findByActiveTrue(pageable);

        List<ProductResponse> products = productPage.getContent()
                .stream()
                .map(this::mapToProductResponse)
                .toList();

        return new PageResponse<>(products, productPage.getNumber(), productPage.getSize(),
                productPage.getTotalElements(), productPage.getTotalPages());
    }


    public List<ProductResponse> getAllProducts() {
        return productRepository.findByActiveTrue().stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @CacheEvict(value = {"product", "products"}, key = "#id", allEntries = true)
    public boolean deleteProduct(Long id) {
        return productRepository.findById(id)
                .map(product -> {
                    product.setActive(false);
                    productRepository.save(product);
                    return true;
                }).orElse(false);
    }

    public List<ProductResponse> searchProducts(String keyword) {
        return productRepository.searchProducts(keyword).stream()
                .map(this::mapToProductResponse)
                .collect(Collectors.toList());
    }

    @Cacheable(value = "product", key = "#id")
    public Optional<ProductResponse> getProductById(Long id) {
        System.out.println("DB is called");
        return productRepository.findByIdAndActiveTrue(id)
                .map(this::mapToProductResponse);
    }

    public boolean updateProductQuantityById(Long id, int quantity) {
        Optional<Product> optionalProduct = productRepository.findByIdAndActiveTrue(id);
        if (optionalProduct.isPresent()){
            Product product = optionalProduct.get();
            System.out.println(product.getStockQuantity());
            product.setStockQuantity(product.getStockQuantity()-quantity);
            System.out.println(product.getStockQuantity());
            Product product1 = productRepository.save(product);
            if (product1 != null){
                return true;
            }
        }
        return false;
    }

    public boolean updateProductQuantity(OrderCreatedEvent event) {
        for (OrderItemDTO item : event.getItems()) {
            boolean updated = updateProductQuantityById(item.getProductId(), item.getQuantity());
            if (!updated) {
                return false;
            }
        }
        return true;
    }
}
