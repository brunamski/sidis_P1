package com.example.projeto.domain.services;

import com.example.projeto.domain.models.Product;
import com.example.projeto.exceptions.FileStorageException;
import com.example.projeto.exceptions.MyResourceNotFoundException;
import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import java.util.UUID;

@Service
public class FileStorageService {

    private static final Font BARCODE_TEXT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 14);
    private final Path fileStorageLocation;

    @Autowired
    private ProductService productService;

    @Autowired
    public FileStorageService(final FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();

        try {
            Files.createDirectories(fileStorageLocation);
        } catch (final Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.",
                    ex);
        }
    }

    public String storeFile(final MultipartFile file) {

        UUID uuid = UUID.randomUUID();
        String fileName = uuid.toString() + ".png";

        // Copy file to the target location (Replacing existing file with the same name)
        try {
            if(fileName.contains("..")){
                throw new FileStorageException("Invalid path." + fileName);
            }


            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (final IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    private String determineFileName(final MultipartFile file) {
//		// Normalize file name
//		final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
//		// Check if the file's name contains invalid characters
//		if (fileName.contains("..")) {
//			throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
//		}
//		return fileName;

        return UUID.randomUUID().toString() + "." + getExtension(file.getOriginalFilename()).orElse("");
    }

    public Optional<String> getExtension(final String filename) {
        return Optional.ofNullable(filename).filter(f -> f.contains("."))
                .map(f -> f.substring(filename.lastIndexOf(".") + 1));
    }

    public Resource loadFileAsResource(final String fileName) {
        try {
            final Path filePath = fileStorageLocation.resolve(fileName).normalize();
            final Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()) {
                return resource;
            }
            throw new MyResourceNotFoundException("File not found " + fileName);
        } catch (final MalformedURLException ex) {
            throw new MyResourceNotFoundException("File not found " + fileName, ex);
        }
    }

    public BufferedImage getBarcode(String sku) throws Exception {
        Optional<Product> optionalProduct = productService.findBySku(sku);
        if(!optionalProduct.isPresent()){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product Not Found");
        }
        Barcode barcode = BarcodeFactory.createCode128(sku);
        barcode.setFont(BARCODE_TEXT_FONT);
        barcode.setLabel(sku);
        BufferedImage image = BarcodeImageHandler.getImage(barcode);

        return image;
    }
}
