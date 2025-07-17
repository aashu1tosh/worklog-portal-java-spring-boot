package com.backend.hrms.config.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import com.backend.hrms.constants.enums.MediaType;

@Component
public class StringToMediaTypeConverter implements Converter<String, MediaType> {
    @Override
    public MediaType convert(@NonNull String source) {
        return MediaType.fromValue(source);
    }
}
