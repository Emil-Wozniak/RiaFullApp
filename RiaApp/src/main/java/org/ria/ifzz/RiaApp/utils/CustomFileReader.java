package org.ria.ifzz.RiaApp.utils;

import org.springframework.web.multipart.MultipartFile;

public class CustomFileReader {

    public String fileContent(MultipartFile file){

        String completeData = "";

        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                completeData = new String(bytes);
                String[] rows = completeData.split("#");
                String[] columns = rows[0].split(",");

            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }

        return completeData;
    }
}
