package com.example.demo.users;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    @Mapping(source = "banned", target = "banned")
    User mapToModel(UserEntity userEntity);
    UserEntity mapToEntity(User userModel);
}
