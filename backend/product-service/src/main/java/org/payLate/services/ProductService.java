package org.payLate.services;

import jakarta.transaction.Transactional;
import org.payLate.dto.AliExpressProductDTO;
import org.payLate.dto.AmazonProductDTO;
import org.payLate.dto.OlxProductDTO;
import org.payLate.dto.ProductDTO;
import org.payLate.entity.AliExpressProduct;
import org.payLate.entity.AmazonProduct;
import org.payLate.entity.Author;
import org.payLate.entity.OlxProduct;
import org.payLate.entity.Product;
import org.payLate.repository.*;
import org.payLate.requestModels.AddProductRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
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



    public Optional<AmazonProductDTO> getAmazonProductDTOById(Long id) {
        return amazonProductRepository.findById(id).map(this::toAmazonProductDTO);
    }

    public Optional<OlxProductDTO> getOlxProductDTOById(Long id) {
        return olxProductRepository.findById(id).map(this::toOlxProductDTO);
    }

    public Optional<AliExpressProductDTO> getAliExpressProductDTOById(Long id) {
        return aliExpressProductRepository.findById(id).map(this::toAliExpressProductDTO);
    }

    public Optional<ProductDTO> getProductDTOById(Long id) {
        return productRepository.findById(id).map(this::toProductDTO);
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

    public void addProductForPartner(String partner, AddProductRequest addProductRequest) {
        switch (partner.toLowerCase()) {
            case "olx" -> {
                OlxProductDTO dto = new OlxProductDTO();
                dto.setProductName(addProductRequest.getProductName());
                dto.setCategory(addProductRequest.getCategory());
                dto.setPrice(addProductRequest.getPrice());
                dto.setImgUrl(addProductRequest.getImgUrl());
                dto.setDescription(addProductRequest.getDescription());
                dto.setQuantity(addProductRequest.getQuantity() != null ? addProductRequest.getQuantity().intValue() : 1);
                dto.setRating(addProductRequest.getRating());
                dto.setAuthorName(addProductRequest.getAuthorName());
                dto.setAuthorUrl(addProductRequest.getAuthorUrl());
                saveOlxProducts(List.of(dto));
            }
            case "aliexpress" -> {
                AliExpressProductDTO dto = new AliExpressProductDTO();
                dto.setProductName(addProductRequest.getProductName());
                dto.setCategory(addProductRequest.getCategory());
                dto.setPrice(addProductRequest.getPrice());
                dto.setImgUrl(addProductRequest.getImgUrl());
                dto.setDescription(addProductRequest.getDescription());
                dto.setQuantity(addProductRequest.getQuantity() != null ? addProductRequest.getQuantity().intValue() : 1);
                dto.setRating(addProductRequest.getRating());
                dto.setAuthorName(addProductRequest.getAuthorName());
                dto.setAuthorUrl(addProductRequest.getAuthorUrl());
                saveAliexpressProducts(List.of(dto));
            }
            case "amazon" -> {
                AmazonProductDTO dto = new AmazonProductDTO();
                dto.setProductName(addProductRequest.getProductName());
                dto.setCategory(addProductRequest.getCategory());
                dto.setPrice(addProductRequest.getPrice());
                dto.setImgUrl(addProductRequest.getImgUrl());
                dto.setDescription(addProductRequest.getDescription());
                dto.setQuantity(addProductRequest.getQuantity() != null ? addProductRequest.getQuantity().intValue() : 1);
                dto.setRating(addProductRequest.getRating());
                dto.setAuthorName(addProductRequest.getAuthorName());
                dto.setAuthorUrl(addProductRequest.getAuthorUrl());
                saveAmazonProducts(List.of(dto));
            }
            default -> throw new IllegalArgumentException("Unknown partner type: " + partner);
        }
    }

    public boolean amazonProductExists(Long id) {
        return amazonProductRepository.existsById(id);
    }
    public boolean olxProductExists(Long id) {
        return olxProductRepository.existsById(id);
    }
    public boolean aliexpressProductExists(Long id) {
        return aliExpressProductRepository.existsById(id);
    }
    public boolean productExists(Long id) {
        return productRepository.existsById(id);
    }

    public AmazonProductDTO toAmazonProductDTO(AmazonProduct product) {
        AmazonProductDTO dto = new AmazonProductDTO();
        dto.setProductName(product.getProductName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setImgUrl(product.getImgUrl());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getQuantity() != null ? product.getQuantity().intValue() : 1);
        dto.setRating(product.getRating());
        if (product.getAuthor() != null) {
            dto.setAuthorName(product.getAuthor().getAuthorName());
            dto.setAuthorUrl(product.getAuthor().getAuthorUrl());
        }
        return dto;
    }

    public OlxProductDTO toOlxProductDTO(OlxProduct product) {
        OlxProductDTO dto = new OlxProductDTO();
        dto.setProductName(product.getProductName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setImgUrl(product.getImgUrl());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getQuantity() != null ? product.getQuantity().intValue() : 1);
        dto.setRating(product.getRating());
        if (product.getAuthor() != null) {
            dto.setAuthorName(product.getAuthor().getAuthorName());
            dto.setAuthorUrl(product.getAuthor().getAuthorUrl());
        }
        return dto;
    }

    public AliExpressProductDTO toAliExpressProductDTO(AliExpressProduct product) {
        AliExpressProductDTO dto = new AliExpressProductDTO();
        dto.setProductName(product.getProductName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setImgUrl(product.getImgUrl());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getQuantity() != null ? product.getQuantity().intValue() : 1);
        dto.setRating(product.getRating());
        if (product.getAuthor() != null) {
            dto.setAuthorName(product.getAuthor().getAuthorName());
            dto.setAuthorUrl(product.getAuthor().getAuthorUrl());
        }
        return dto;
    }

    public ProductDTO toProductDTO(Product product) {
        ProductDTO dto = new ProductDTO();
        dto.setProductName(product.getProductName());
        dto.setCategory(product.getCategory());
        dto.setPrice(product.getPrice());
        dto.setImgUrl(product.getImgUrl());
        dto.setDescription(product.getDescription());
        dto.setQuantity(product.getQuantity() != null ? product.getQuantity().intValue() : 1);
        dto.setRating(product.getRating());
        return dto;
    }
}