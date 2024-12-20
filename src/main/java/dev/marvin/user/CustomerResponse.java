package dev.marvin.user;

import dev.marvin.address.AddressResponse;

public record CustomerResponse(String name, String mobile, AddressResponse addressResponse) {
}
