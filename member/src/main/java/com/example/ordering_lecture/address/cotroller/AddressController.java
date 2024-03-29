package com.example.ordering_lecture.address.cotroller;

import com.example.ordering_lecture.address.dto.AddressRequestDto;
import com.example.ordering_lecture.address.dto.AddressResponseDto;
import com.example.ordering_lecture.address.dto.AddressUpdateDto;
import com.example.ordering_lecture.address.service.AddressService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/member_server/address")
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping("/create/{id}")
    public String createAddress(@PathVariable Long id, @RequestBody AddressRequestDto addressRequestDto) {
        addressService.createAddress(id, addressRequestDto);
        return "ok";
    }

    @GetMapping("/addresses")
    public List<AddressResponseDto> addresses() {
        return addressService.showAllAddress();
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
