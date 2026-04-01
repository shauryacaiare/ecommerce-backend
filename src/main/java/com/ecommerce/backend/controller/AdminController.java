package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.requestdto.CompanyReqDto;
import com.ecommerce.backend.dto.requestdto.CreateCategoryReqDto;
import com.ecommerce.backend.dto.requestdto.CreateSellerRequestDto;
import com.ecommerce.backend.dto.responsedto.CompanyResponseDto;
import com.ecommerce.backend.dto.responsedto.CreateCategoryResDto;
import com.ecommerce.backend.dto.responsedto.CreateSellerResponseDto;
import com.ecommerce.backend.service.AdminService;
import com.ecommerce.backend.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;
    private final CategoryService categoryService;

    @PostMapping("/seller")
    public ResponseEntity<CreateSellerResponseDto> createSeller(@Valid @RequestBody CreateSellerRequestDto requestDto){

        log.info("create seller request received {}",requestDto);
        CreateSellerResponseDto responseDto = adminService.saveSeller(requestDto);
        return new ResponseEntity<CreateSellerResponseDto>(responseDto, HttpStatus.CREATED);

    }

    @GetMapping("/all-sellers")
    public ResponseEntity<List<CreateSellerResponseDto>> getSellers(){
        List<CreateSellerResponseDto> responseDtos = adminService.getAllSellers();
        return new ResponseEntity<>(responseDtos,HttpStatus.OK);
    }

    @DeleteMapping("/delete-seller/{id}")
    public ResponseEntity<CreateSellerResponseDto> deleteSeller(@PathVariable("id") Long id) {
        log.info("Delete request received for seller: {}", id);

        CreateSellerResponseDto responseDto = adminService.deleteSeller(id);

        return ResponseEntity.ok(responseDto);
    }

    @PostMapping("/company")
    public ResponseEntity<CompanyResponseDto> createCompany(@Valid @RequestBody CompanyReqDto reqDto){
        log.info("create company received {}",reqDto);
        CompanyResponseDto companyResponseDto = adminService.createCompany(reqDto);
        return new ResponseEntity<CompanyResponseDto> (companyResponseDto,HttpStatus.CREATED);
    }

    @PostMapping("/category")
    public ResponseEntity<CreateCategoryResDto> createCategory(@Valid @RequestBody CreateCategoryReqDto createCategoryReqDto){

        log.info("new category creation request: {}",createCategoryReqDto);
        CreateCategoryResDto responseDto = categoryService.createCategory(createCategoryReqDto);
        return new ResponseEntity<>(responseDto,HttpStatus.CREATED);
    }
}
