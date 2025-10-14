package com.application.mapper;

import org.hibernate.collection.spi.PersistentCollection;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class ObjectUtils {

    ObjectUtils() {
    }

    @Bean
    public static ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setFieldMatchingEnabled(true).setPropertyCondition(context ->
                        !(context.getSource() instanceof PersistentCollection)
                );
        return modelMapper;
    }

    /**
     * Maps {@code source} to {@code destination}.
     *
     * @param source      object to map from
     * @param destination object to map to
     */
    public static Object map(Object source, Object destination) {
        if (source == null) {
            return null;
        }

        modelMapper().map(source, destination);
        return destination;
    }

    public static Boolean listIsEmpty(List<? extends Object> data) {
        return data == null || data.isEmpty();
    }

    public static Boolean isEmptyOrNull(String obj) {
        return obj == null || obj.isEmpty();
    }

}
