package org.payLate.services;


import jakarta.transaction.Transactional;
import org.payLate.dto.AliExpressProductDTO;
import org.payLate.dto.AmazonProductDTO;
import org.payLate.dto.OlxProductDTO;
import org.payLate.entity.AliExpressProduct;
import org.payLate.entity.AmazonProduct;
import org.payLate.entity.Author;
import org.payLate.entity.OlxProduct;
import org.payLate.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductService {
    @Value("${csv.file.pathOlx}")
    private String csvFilePath;

    private final ProductRepository productRepository;

    private final AliExpressProductRepository aliExpressProductRepository;
    private final OlxProductRepository olxProductRepository;

    private final AmazonProductRepository amazonProductRepository;

    private final AuthorRepository authorRepository;

    private final CSVService csvService;

    public ProductService(ProductRepository productRepository, OlxProductRepository olxProductRepository,
                          CSVService csvService, AuthorRepository authorRepository,
                          AliExpressProductRepository aliExpressProductRepository, AmazonProductRepository amazonProductRepository) {
        this.productRepository = productRepository;
        this.olxProductRepository = olxProductRepository;
        this.csvService = csvService;
        this.authorRepository = authorRepository;
        this.aliExpressProductRepository = aliExpressProductRepository;
        this.amazonProductRepository = amazonProductRepository;
    }

    public void saveOlxProducts(List<OlxProductDTO> olxProductDTOs) {
        List<OlxProduct> olxProducts = olxProductDTOs.stream()
                .map(dto -> {
                    OlxProduct olxProduct = new OlxProduct();
                    olxProduct.setProductName(dto.getProductName());
                    olxProduct.setPrice(dto.getPrice());
                    olxProduct.setDescription(dto.getDescription());
                    olxProduct.setCategory(dto.getCategory());
                    olxProduct.setQuantity(dto.getQuantity() != null ? dto.getQuantity().longValue() : 1L);
                    olxProduct.setImgUrl(dto.getImgUrl());
                    olxProduct.setRating(dto.getRating());

                    if (dto.getAuthorName() != null) {
                        Author author = authorRepository.findByAuthorName(dto.getAuthorName())
                                .orElseGet(() -> {
                                    Author currAuthor = new Author();
                                    currAuthor.setAuthorName(dto.getAuthorName());
                                    currAuthor.setAuthorUrl(dto.getAuthorUrl());
                                    return authorRepository.save(currAuthor);
                                });
                        olxProduct.setAuthor(author);
                    }

                    return olxProduct;
                })
                .collect(Collectors.toList());

        olxProductRepository.saveAll(olxProducts);
    }

    public void saveAliexpressProducts(List<AliExpressProductDTO> aliexpressProductDTOs) {
        List<AliExpressProduct> aliexpressProducts = aliexpressProductDTOs.stream()
                .map(dto -> {
                    AliExpressProduct aliexpressProduct = new AliExpressProduct();
                    aliexpressProduct.setProductName(dto.getProductName());
                    aliexpressProduct.setPrice(dto.getPrice());
                    aliexpressProduct.setDescription(dto.getDescription());
                    aliexpressProduct.setCategory(dto.getCategory());
                    aliexpressProduct.setQuantity(dto.getQuantity() != null ? dto.getQuantity().longValue() : 1L);
                    aliexpressProduct.setImgUrl(dto.getImgUrl());
                    aliexpressProduct.setRating(dto.getRating());

                    if (dto.getAuthorName() != null) {
                        Author author = authorRepository.findByAuthorName(dto.getAuthorName())
                                .orElseGet(() -> {
                                    Author currAuthor = new Author();
                                    currAuthor.setAuthorName(dto.getAuthorName());
                                    currAuthor.setAuthorUrl(dto.getAuthorUrl());
                                    return authorRepository.save(currAuthor);
                                });
                        aliexpressProduct.setAuthor(author);
                    }

                    return aliexpressProduct;
                })
                .collect(Collectors.toList());

        aliExpressProductRepository.saveAll(aliexpressProducts);
    }

    public void saveAmazonProducts(List<AmazonProductDTO> amazonProductDTOs) {
        List<AmazonProduct> amazonProducts = amazonProductDTOs.stream()
                .map(dto -> {
                    AmazonProduct amazonProduct = new AmazonProduct();
                    amazonProduct.setProductName(dto.getProductName());
                    amazonProduct.setPrice(dto.getPrice());
                    amazonProduct.setDescription(dto.getDescription());
                    amazonProduct.setCategory(dto.getCategory());
                    amazonProduct.setQuantity(dto.getQuantity() != null ? dto.getQuantity().longValue() : 1L);
                    amazonProduct.setImgUrl(dto.getImgUrl());
                    amazonProduct.setRating(dto.getRating());

                    if (dto.getAuthorName() != null) {
                        Author author = authorRepository.findByAuthorName(dto.getAuthorName())
                                .orElseGet(() -> {
                                    Author currAuthor = new Author();
                                    currAuthor.setAuthorName(dto.getAuthorName());
                                    currAuthor.setAuthorUrl(dto.getAuthorUrl());
                                    return authorRepository.save(currAuthor);
                                });
                        amazonProduct.setAuthor(author);
                    }

                    return amazonProduct;
                })
                .collect(Collectors.toList());

        amazonProductRepository.saveAll(amazonProducts);
    }

//    public void increaseProductQuantity(Long productId) throws Exception {
//        Optional<Product> product = productRepository.findById(productId);
//
//        if (!product.isPresent()) {
//            throw new Exception("Product not found!");
//        }
//
//        product.get().setQuantity(product.get().getQuantity() + 1);
//
//        productRepository.save(product.get());
//    }
//
//    public void decreaseProductQuantity(Long productId) throws Exception {
//        Optional<Product> product = productRepository.findById(productId);
//
//        if (!product.isPresent() || product.get().getQuantity() <= 0) {
//            throw new Exception("Product not found or quantity locked!");
//        }
//
//        product.get().setQuantity(product.get().getQuantity() - 1);
//
//        productRepository.save(product.get());
//    }
}