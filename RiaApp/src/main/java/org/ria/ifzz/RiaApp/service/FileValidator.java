package org.ria.ifzz.RiaApp.service;

import org.ria.ifzz.RiaApp.domain.FileModel;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class FileValidator implements Validator {

    @Override
    public boolean supports(Class<?> clazz) {
        return FileModel.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        FileModel fileModel = (FileModel) target;

        if (fileModel.getFile() != null && fileModel.getFile().isEmpty()){
            errors.rejectValue("file", "file.empty");
        }
    }
}
