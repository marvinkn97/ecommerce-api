package dev.marvin.address;

import dev.marvin.exception.RequestValidationException;
import dev.marvin.shared.Mapper;
import dev.marvin.user.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final AddressUtils addressUtils;

    @Override
    public void addAddress(AddressRequest addressRequest, UserEntity userEntity) {
        log.info("Inside addAddress method of AddressServiceImpl");
        Address address = new Address();
        address.setCounty(addressRequest.county());
        address.setTown(addressRequest.town());
        address.setStreet(addressRequest.street());
        address.setBuilding(addressRequest.building());
        address.setUserEntity(userEntity);
        addressRepository.save(address);
        log.info("Address added successfully");
    }

    @Override
    public AddressResponse getOne(UserEntity userEntity) {
        Address address = addressUtils.getAddressByUserId(userEntity.getId());
        return Mapper.mapToDto(address);
    }

    @Override
    public void update(UserEntity userEntity, AddressUpdateRequest addressRequest) {
        log.info("Inside update method of AddressServiceImpl");
        Address address = addressUtils.getAddressByUserId(userEntity.getId());
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
        log.info("Address updated successfully");
    }

    @Override
    public void delete(UserEntity userEntity) {
        log.info("Inside deleteAddress method of AddressServiceImpl");
        Address address = addressUtils.getAddressByUserId(userEntity.getId());
        addressRepository.delete(address);
        log.info("Address deleted successfully");
    }

}
