package dev.marvin.utils;

import dev.marvin.domain.Address;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressUtils {
    private final AddressRepository addressRepository;

    public Address getAddressByUserId(Integer userId){
        return addressRepository.findByUserId(userId).orElseThrow(()-> new ResourceNotFoundException("Address not found"));
    }

}
