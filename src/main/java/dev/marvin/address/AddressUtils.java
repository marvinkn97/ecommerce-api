package dev.marvin.address;

import dev.marvin.exception.ResourceNotFoundException;
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
