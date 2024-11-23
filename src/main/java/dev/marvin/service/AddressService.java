package dev.marvin.service;

import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddressRequest;
import dev.marvin.dto.AddressResponse;
import org.springframework.data.domain.Page;

import java.util.Collection;

public interface AddressService {
    void addAddress(AddressRequest addressRequest, UserEntity userEntity);
    Collection<AddressResponse> getAll();

    Page<AddressResponse> getAllPaginated();

    AddressResponse getOne(Integer addressId);

    void update(Integer addressId, AddressRequest addressRequest);

}
