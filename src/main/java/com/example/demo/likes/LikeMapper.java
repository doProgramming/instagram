package com.example.demo.likes;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel="spring")
public interface LikeMapper {
    @Mapping(source = "id", target = "id")
    Like mapToModel(LikeEntity likeEntity);
    @Mapping(source = "id", target = "id")
    LikeEntity mapToEntity(Like likeModel);
}
