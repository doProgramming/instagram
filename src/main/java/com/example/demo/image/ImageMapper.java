package com.example.demo.image;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ImageMapper {

    Image mapToModel(ImageEntity entity);
    @Mapping(target = "urlLink", source = "urlLink")
    ImageEntity mapToEntity(Image image);
}
