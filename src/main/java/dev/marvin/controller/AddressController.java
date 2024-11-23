package dev.marvin.controller;

import dev.marvin.constants.MessageConstants;
import dev.marvin.domain.UserEntity;
import dev.marvin.dto.AddressRequest;
import dev.marvin.dto.ResponseDto;
import dev.marvin.service.AddressService;
import dev.marvin.utils.AuthUtils;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/address")
@RequiredArgsConstructor
@Slf4j
public class AddressController {
    private final AddressService addressService;
    private final AuthUtils authUtils;

    @PostMapping("/addresses")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public ResponseEntity<ResponseDto<String>> add(@Valid @RequestBody AddressRequest addressRequest) {
        log.info("Inside add method of AddressController");
        UserEntity userEntity = authUtils.getAuthenticatedUserEntity();
        addressService.addAddress(addressRequest, userEntity);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ResponseDto<>(HttpStatus.CREATED.getReasonPhrase(), MessageConstants.ADDRESS_CREATED));
    }

}
