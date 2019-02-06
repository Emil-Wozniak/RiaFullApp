package org.ria.ifzz.RiaApp.services;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("services")
public class StorageProperties {

    /**
     * Folder location for storing files
     */
    private String location = "upload-dir";

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

}
