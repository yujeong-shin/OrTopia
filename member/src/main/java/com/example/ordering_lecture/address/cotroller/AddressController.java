package com.example.ordering_lecture.address.cotroller;

import com.example.ordering_lecture.address.dto.AddressRequestDto;
import com.example.ordering_lecture.address.dto.AddressResponseDto;
import com.example.ordering_lecture.address.dto.AddressUpdateDto;
import com.example.ordering_lecture.address.service.AddressService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/create/{email}")
    public String createAddress(@PathVariable String email, @RequestBody AddressRequestDto addressRequestDto) {
        addressService.createAddress(email, addressRequestDto);
        return "ok";
    }

    @GetMapping("/addresses")
    public List<AddressResponseDto> addresses() {
        return addressService.showAllAddress();
    }
    @GetMapping("/address/{id}")
    public Object findAddress(@PathVariable Long id) {
        return addressService.findById(id);
    }
    @GetMapping("/members/{email}/addresses")
    public ResponseEntity<List<AddressResponseDto>> allAddressesByEmail(@PathVariable String email) {
        List<AddressResponseDto> addresses = addressService.findAllAddressesByEmail(email);
        return ResponseEntity.ok(addresses);
    }

    @PatchMapping("/update/{id}")
    public AddressResponseDto updateAddress(@PathVariable Long id, @RequestBody AddressUpdateDto addressUpdateDto) {
        return addressService.updateAddress(id, addressUpdateDto);
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAddress(@PathVariable Long id) {
        addressService.deleteAddress(id);
        return "ok";
    }
}