package dev.marvin.address;

import dev.marvin.auth.AuthUtils;
import dev.marvin.constants.MessageConstants;
import dev.marvin.shared.ResponseDto;
import dev.marvin.user.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${app.api.prefix}/address")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Address Resource", description = "CRUD operations for managing user addresses")
public class AddressController {
    private final AddressService addressService;
    private final AuthUtils authUtils;

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Add a new address for the authenticated user")
    public ResponseEntity<ResponseDto<String>> add(@Valid @RequestBody AddressRequest addressRequest) {
        log.info("Inside add method of AddressController");
        UserEntity userEntity = authUtils.getAuthenticatedUserEntity();
        addressService.addAddress(addressRequest, userEntity);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), MessageConstants.ADDRESS_CREATED));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Get the address of the authenticated user")
    public ResponseEntity<ResponseDto<Object>> getAddress() {
        log.info("Inside getAddress method of AddressController");
        UserEntity userEntity = authUtils.getAuthenticatedUserEntity();
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), addressService.getOne(userEntity)));
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PutMapping
    @Operation(summary = "Update the address of the authenticated user")
    public ResponseEntity<ResponseDto<String>> updateAddress(@Valid @RequestBody AddressUpdateRequest updateRequest) {
        log.info("Inside updateAddress method of AddressController");
        UserEntity userEntity = authUtils.getAuthenticatedUserEntity();
        addressService.update(userEntity, updateRequest);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.ADDRESS_UPDATED));
    }

    @DeleteMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @Operation(summary = "Delete the address of the authenticated user")
    public ResponseEntity<ResponseDto<String>> deleteAddress() {
        log.info("Inside deleteAddress method of AddressController");
        UserEntity userEntity = authUtils.getAuthenticatedUserEntity();
        addressService.delete(userEntity);
        return ResponseEntity.ok(new ResponseDto<>(HttpStatus.OK.getReasonPhrase(), MessageConstants.ADDRESS_DELETED));
    }
}
