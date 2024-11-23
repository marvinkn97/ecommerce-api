package dev.marvin.utils;

import dev.marvin.repository.AddressRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AddressUtils {
    private final AddressRepository addressRepository;

}
