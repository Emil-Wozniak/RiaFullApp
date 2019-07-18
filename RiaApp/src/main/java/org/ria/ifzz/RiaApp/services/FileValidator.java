package org.ria.ifzz.RiaApp.services;

import org.ria.ifzz.RiaApp.models.DataFileMetadata;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FileValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return DataFileMetadata.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        DataFileMetadata dataFileMetadata = (DataFileMetadata) target;

        if (dataFileMetadata.getFile() != null && dataFileMetadata.getFile().isEmpty()){
            errors.rejectValue("file", "file.empty");
        }
    }
}
