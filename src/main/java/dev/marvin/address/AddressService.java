package dev.marvin.address;

import dev.marvin.address.AddressRequest;
import dev.marvin.address.AddressResponse;
import dev.marvin.address.AddressUpdateRequest;
import dev.marvin.user.UserEntity;

public interface AddressService {
    void addAddress(AddressRequest addressRequest, UserEntity userEntity);

    AddressResponse getOne(UserEntity userEntity);

    void update(UserEntity userEntity, AddressUpdateRequest addressRequest);

    void delete(UserEntity userEntity);

}
