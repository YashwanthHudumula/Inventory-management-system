package com.yashwanth.inventory.management.system.services.impl;

import com.yashwanth.inventory.management.system.dtos.ProductDTO;
import com.yashwanth.inventory.management.system.dtos.Response;
import com.yashwanth.inventory.management.system.exceptions.NotFoundException;
import com.yashwanth.inventory.management.system.models.Category;
import com.yashwanth.inventory.management.system.models.Product;
import com.yashwanth.inventory.management.system.repositories.CategoryRepository;
import com.yashwanth.inventory.management.system.repositories.ProductRepository;
import com.yashwanth.inventory.management.system.services.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final CategoryRepository categoryRepository;

    private static final String IMAGE_DIRECTORY = System.getProperty("user.dir") + "/product-images/";

    @Value("${image.upload.dir}")
    private String imageUploadDir;

    @Override
    public Response saveProduct(ProductDTO productDTO, MultipartFile imageFile) {

        Category category = categoryRepository.findById(productDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("Category Not Found"));

        Product productToSave = Product.builder()
                .name(productDTO.getName())
                .sku(productDTO.getSku())
                .price(productDTO.getPrice())
                .stockQuantity(productDTO.getStockQuantity())
                .description(productDTO.getDescription())
                .category(category)
                .build();

        if (imageFile != null && !imageFile.isEmpty()) {
            log.info("✅ Image file exists: {}", imageFile.getOriginalFilename());
            String imagePath = saveImage2(imageFile);
            log.info("✅ Image saved to: {}", imagePath);
            productToSave.setImageUrl(imagePath);
        }

        productRepository.save(productToSave);

        return Response.builder()
                .status(200)
                .message("Product successfully saved")
                .build();
    }

    @Override
    public Response updateProduct(ProductDTO productDTO, MultipartFile imageFile) {
        Product existingProduct = productRepository.findById(productDTO.getProductId())
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        if (imageFile != null && !imageFile.isEmpty()) {
            String imagePath = saveImage2(imageFile);
            existingProduct.setImageUrl(imagePath);
        }

        if (productDTO.getCategoryId() != null && productDTO.getCategoryId() > 0) {
            Category category = categoryRepository.findById(productDTO.getCategoryId())
                    .orElseThrow(() -> new NotFoundException("Category Not Found"));
            existingProduct.setCategory(category);
        }

        if (productDTO.getName() != null && !productDTO.getName().isBlank()) {
            existingProduct.setName(productDTO.getName());
        }

        if (productDTO.getSku() != null && !productDTO.getSku().isBlank()) {
            existingProduct.setSku(productDTO.getSku());
        }

        if (productDTO.getDescription() != null && !productDTO.getDescription().isBlank()) {
            existingProduct.setDescription(productDTO.getDescription());
        }

        if (productDTO.getPrice() != null && productDTO.getPrice().compareTo(BigDecimal.ZERO) >= 0) {
            existingProduct.setPrice(productDTO.getPrice());
        }

        if (productDTO.getStockQuantity() != null && productDTO.getStockQuantity() >= 0) {
            existingProduct.setStockQuantity(productDTO.getStockQuantity());
        }

        productRepository.save(existingProduct);

        return Response.builder()
                .status(200)
                .message("Product Updated successfully")
                .build();
    }

    @Override
    public Response getAllProducts() {
        List<Product> productList = productRepository.findAll(Sort.by(Sort.Direction.DESC, "id"));
        List<ProductDTO> productDTOList = modelMapper.map(productList, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOList)
                .build();
    }

    @Override
    public Response getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        return Response.builder()
                .status(200)
                .message("success")
                .product(modelMapper.map(product, ProductDTO.class))
                .build();
    }

    @Override
    public Response deleteProduct(Long id) {
        productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Product Not Found"));

        productRepository.deleteById(id);

        return Response.builder()
                .status(200)
                .message("Product Deleted successfully")
                .build();
    }

    @Override
    public Response searchProduct(String input) {
        List<Product> products = productRepository.findByNameContainingOrDescriptionContaining(input, input);

        if (products.isEmpty()) {
            throw new NotFoundException("Product Not Found");
        }

        List<ProductDTO> productDTOList = modelMapper.map(products, new TypeToken<List<ProductDTO>>() {}.getType());

        return Response.builder()
                .status(200)
                .message("success")
                .products(productDTOList)
                .build();
    }

    private String saveImage2(MultipartFile imageFile) {
        if (!imageFile.getContentType().startsWith("image/") || imageFile.getSize() > 1024L * 1024 * 1024) {
            throw new IllegalArgumentException("Only image files under 1GB are allowed");
        }

        File directory = new File(imageUploadDir);
        if (!directory.exists()) {
            boolean created = directory.mkdirs();
            if (created) {
                log.info("✅ Directory created at {}", imageUploadDir);
            } else {
                log.error("❌ Failed to create directory at {}", imageUploadDir);
                throw new IllegalArgumentException("Could not create directory for image upload");
            }
        }

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        String imagePath = imageUploadDir + "/" + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving Image: " + e.getMessage());
        }

        return "products/" + uniqueFileName;
    }

    private String saveImage(MultipartFile imageFile) {
        // Keeping this method untouched for fallback/local storage if needed
        if (!imageFile.getContentType().startsWith("image/") || imageFile.getSize() > 1024 * 1024 * 1024) {
            throw new IllegalArgumentException("Only image files under 1GB are allowed");
        }

        File directory = new File(IMAGE_DIRECTORY);
        if (!directory.exists()) {
            directory.mkdir();
            log.info("Directory was created");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + imageFile.getOriginalFilename();
        String imagePath = IMAGE_DIRECTORY + uniqueFileName;

        try {
            File destinationFile = new File(imagePath);
            imageFile.transferTo(destinationFile);
        } catch (Exception e) {
            throw new IllegalArgumentException("Error saving Image: " + e.getMessage());
        }

        return imagePath;
    }
}
