package com.RPMS.controller;

import com.vaadin.flow.component.upload.SucceededEvent;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;

import java.io.FileOutputStream;
import java.util.UUID;

public class FileController {
    public static String UploadFiles(SucceededEvent event, MultiFileMemoryBuffer buffer) {
        String filePath = "src/main/webapp/frontend/img/property_uploads/";
        try {
            byte[] bufferInput = new byte[buffer.getInputStream(event.getFileName()).available()];
            buffer.getInputStream(event.getFileName()).read(bufferInput);
            String fileName = UUID.randomUUID() + event.getFileName();

            FileOutputStream fileOutputStream = new FileOutputStream(filePath + fileName);
            fileOutputStream.write(bufferInput);
            fileOutputStream.close();
            return fileName;
        } catch (Exception e) {
            System.out.println(e);
        }
        return "";
    }
}
