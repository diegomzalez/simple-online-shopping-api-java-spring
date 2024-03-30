package com.diegomzalez.productservice.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import com.diegomzalez.productservice.dto.ProductRequest;
import com.diegomzalez.productservice.dto.ProductResponse;
import com.diegomzalez.productservice.model.Product;
import com.diegomzalez.productservice.repository.ProductRepository;
import com.mongodb.client.result.UpdateResult;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;

    public void createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();
        if (product != null) {
            productRepository.save(product);
            log.info("Product created successfully");
        } else
            log.error("Product not created successfully");

    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapProductResponse).toList();
    }

    private ProductResponse mapProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    public ProductResponse getProductById(String productId) {
        Product product = productRepository.findById(productId).orElse(null);
        return mapProductResponse(product);
    }

    public void updateProduct(String productId, ProductRequest productRequest) {
        updateProductAlgorithm(productId, productRequest);
    }

    private final MongoTemplate mongoTemplate;

    public void updateProductAlgorithm(String productId, ProductRequest productRequest) {
        Query query = new Query(Criteria.where("id").is(productId));
        Update update = new Update();

        if (productRequest.getName() != null) {
            update.set("name", productRequest.getName());
        }
        if (productRequest.getDescription() != null) {
            update.set("description", productRequest.getDescription());
        }
        if (productRequest.getPrice() != null) {
            update.set("price", productRequest.getPrice());
        }

        UpdateResult result = mongoTemplate.updateFirst(query, update, Product.class);
        if (result.getModifiedCount() > 0) {
            log.info("Product updated successfully");
        } else {
            log.error("Product not updated successfully");
        }
    }

    public void deleteProduct(String productId) {
        if (productId != null) {
            productRepository.deleteById(productId);
            log.info("Product deleted successfully");
        } else {
            log.error("Product not deleted successfully");
        }
    }
}
