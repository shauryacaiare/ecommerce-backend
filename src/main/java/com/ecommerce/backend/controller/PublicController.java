package com.ecommerce.backend.controller;

import com.ecommerce.backend.dto.responsedto.CreateCategoryResDto;
import com.ecommerce.backend.service.CategoryService;
import com.ecommerce.backend.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/public")
@Slf4j
@RequiredArgsConstructor
public class PublicController {

    private final CategoryService categoryService;

    private final ProductService productService;

    @GetMapping("/categories")
    public ResponseEntity<List<CreateCategoryResDto>> getAllCategories(){

        List<CreateCategoryResDto> categories = categoryService.getAllCategories();
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
}
