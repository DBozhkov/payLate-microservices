package org.payLate.services;

import jakarta.transaction.Transactional;
import org.payLate.dto.*;
import org.payLate.entity.*;
import org.payLate.repository.*;
import org.payLate.requestModels.AddProductRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
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
        saveProducts(olxProductDTOs, OlxProduct::new, olxProductRepository);
    }

    public void saveAliexpressProducts(List<AliExpressProductDTO> aliexpressProductDTOs) {
        saveProducts(aliexpressProductDTOs, AliExpressProduct::new, aliExpressProductRepository);
    }

    public void saveAmazonProducts(List<AmazonProductDTO> amazonProductDTOs) {
        saveProducts(amazonProductDTOs, AmazonProduct::new, amazonProductRepository);
    }

    private <T extends BaseProduct> void saveProducts(
            List<? extends ProductDTOInterface> dtos,
            Supplier<T> entitySupplier,
            JpaRepository<T, Long> repository) {

        List<T> products = dtos.stream()
                .map(dto -> {
                    T entity = entitySupplier.get();
                    entity.setProductName(dto.getProductName());
                    entity.setPrice(dto.getPrice());
                    entity.setDescription(dto.getDescription());
                    entity.setCategory(dto.getCategory());
                    entity.setQuantity(dto.getQuantity() != null ? dto.getQuantity().longValue() : 1L);
                    entity.setImgUrl(dto.getImgUrl());
                    entity.setRating(dto.getRating());

                    if (dto.getAuthorName() != null && !dto.getAuthorName().isBlank()) {
                        Author author = findOrCreateAuthor(dto.getAuthorName(), dto.getAuthorUrl());
                        entity.setAuthor(author);
                    }
                    return entity;
                })
                .collect(Collectors.toList());
        repository.saveAll(products);
    }

    private Author findOrCreateAuthor(String name, String url) {
        return authorRepository.findByAuthorName(name)
                .orElseGet(() -> {
                    Author newAuthor = new Author();
                    newAuthor.setAuthorName(name);
                    newAuthor.setAuthorUrl(url);
                    return authorRepository.save(newAuthor);
                });
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