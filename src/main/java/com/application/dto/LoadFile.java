package com.application.dto;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class LoadFile {
    private String filename;
    private String fileType;
    private String fileSize;
    private byte[] file;
}
