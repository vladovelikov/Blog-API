package com.blog.services.impl;

import com.blog.exceptions.IncorrectFileFormatException;
import com.blog.services.FileService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

    public FileServiceImpl() {
    }

    @Override
    public String uploadImage(String path, MultipartFile image) throws IOException {
        String fileName = image.getOriginalFilename();

        String fileExtension = fileName.substring(fileName.lastIndexOf('.'));

        if (!(fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("jpg")
        || fileExtension.equalsIgnoreCase("jpeg"))) {
            throw new IncorrectFileFormatException("Image file extension is invalid!");
        }

        String randomId = UUID.randomUUID().toString();
        String modifiedImageName = randomId.concat(".").concat(fileExtension);

        String filePath = path + File.separator + modifiedImageName;

        File file = new File(path);

        if (!file.exists()) {
            file.mkdir();
        }

        Files.copy(image.getInputStream(), Path.of(filePath));

        return modifiedImageName;
    }

    @Override
    public InputStream getResources(String path, String imageName) throws FileNotFoundException {
        String filePath = path + File.separator + imageName;
        InputStream inputStream = new FileInputStream(filePath);
        return inputStream;
    }
}
