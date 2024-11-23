package dev.marvin.serviceImpl;

import dev.marvin.domain.Address;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddressRequest;
import dev.marvin.dto.AddressResponse;
import dev.marvin.exception.RequestValidationException;
import dev.marvin.repository.AddressRepository;
import dev.marvin.repository.UserRepository;
import dev.marvin.service.AddressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Collection;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final UserRepository userRepository;
    @Override
    public void addAddress(AddressRequest addressRequest, UserEntity userEntity) {
        log.info("Inside addAddress method of AddressServiceImpl");
        Address address = new Address();
        address.setCounty(addressRequest.county());
        address.setTown(addressRequest.town());
        address.setStreet(addressRequest.street());
        address.setBuilding(addressRequest.building());

        userEntity.getAddresses().add(address);
        userRepository.save(userEntity);
        log.info("Address added successfully");

    }

    @Override
    public Collection<AddressResponse> getAll() {
        return null;
    }

    @Override
    public Page<AddressResponse> getAllPaginated() {
        return null;
    }

    @Override
    public AddressResponse getOne(Integer addressId) {
        return null;
    }

    @Override
    public void update(Integer addressId, AddressRequest addressRequest) {
        log.info("Inside update method of AddressServiceImpl");
        Address address = addressUtils.getAddressById(addressId);
        boolean changes = false;

        String updatedCounty = addressRequest.county();
        if (StringUtils.hasText(updatedCounty) && !updatedCounty.equals(address.getCounty())) {
            log.info("Updating county from '{}' to '{}'", address.getCounty(), updatedCounty);
            address.setCounty(updatedCounty);
            changes = true;
        }

        String updatedTown = addressRequest.town();
        if (StringUtils.hasText(updatedTown) && !updatedTown.equals(address.getTown())) {
            log.info("Updating town from '{}' to '{}'", address.getTown(), updatedTown);
            address.setTown(updatedTown);
            changes = true;
        }

        String updatedStreet = addressRequest.street();
        if (StringUtils.hasText(updatedStreet) && !updatedStreet.equals(address.getStreet())) {
            log.info("Updating street from '{}' to '{}'", address.getStreet(), updatedStreet);
            address.setStreet(updatedStreet);
            changes = true;
        }

        String updatedBuilding = addressRequest.building();
        if (StringUtils.hasText(updatedBuilding) && !updatedBuilding.equals(address.getBuilding())) {
            log.info("Updating building from '{}' to '{}'", address.getBuilding(), updatedBuilding);
            address.setBuilding(updatedBuilding);
            changes = true;
        }

        if (!changes) {
            log.warn("No updates were provided");
            throw new RequestValidationException("No changes detected in the request");
        }

        addressRepository.save(address);
        log.info("Address with ID [{}] successfully updated", addressId);
    }
}
