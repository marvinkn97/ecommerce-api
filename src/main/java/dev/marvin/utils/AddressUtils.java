package dev.marvin.utils;

import dev.marvin.address.Address;
import dev.marvin.exception.ResourceNotFoundException;
import dev.marvin.address.AddressRepository;
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
