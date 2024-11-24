package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddressRequest;
import dev.marvin.dto.AddressResponse;
import dev.marvin.dto.AddressUpdateRequest;

public interface AddressService {
    void addAddress(AddressRequest addressRequest, UserEntity userEntity);

    AddressResponse getOne(UserEntity userEntity);

    void update(UserEntity userEntity, AddressUpdateRequest addressRequest);

    void delete(UserEntity userEntity);

}
