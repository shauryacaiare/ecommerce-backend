package com.ecommerce.backend.service;

import com.ecommerce.backend.dto.requestdto.CreateCategoryReqDto;
import com.ecommerce.backend.dto.responsedto.CreateCategoryResDto;
import com.ecommerce.backend.entity.Category;
import com.ecommerce.backend.excpetion.ResourceAlreadyExistException;
import com.ecommerce.backend.repository.CategoryRepo;
import com.ecommerce.backend.service.utility.Utility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CategoryService {

    private final CategoryRepo categoryRepo;
    private final Utility utility;

    public List<CreateCategoryResDto> getAllCategories() {
        List<Category> categories = categoryRepo.findAll();
        return categories.
                stream()
                .map(utility::mapToCreateCategoryResDto)
                .toList();
    }

    public CreateCategoryResDto createCategory(CreateCategoryReqDto createCategoryReqDto) {
        String name = createCategoryReqDto.getName();
        if(categoryRepo.existsByName(name)){
            throw new ResourceAlreadyExistException("category name",name);
        }
        Category category = Category.builder()
                .name(name)
                .description(createCategoryReqDto.getDesc())
                .build();
        Category savedCategory = categoryRepo.save(category);
        return CreateCategoryResDto.builder()
                .id(savedCategory.getId())
                .name(savedCategory.getName())
                .desc(savedCategory.getDescription())
                .build();

    }

}
