package com.blog.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {

    String uploadImage(String path, MultipartFile image) throws IOException;

    InputStream getResources(String path, String imageName) throws FileNotFoundException;
}
