package com.application.service;

import com.application.dto.LoadFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface MongoFileService {

    String addFile(MultipartFile upload, int manufacturerId, String manufacturerName) throws IOException;

    LoadFile downloadFile(String id) throws IOException;

}
