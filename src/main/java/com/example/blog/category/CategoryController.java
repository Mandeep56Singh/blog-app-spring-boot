package com.example.blog.category;


import com.example.blog.category.dto.CategoryRequestDTO;
import com.example.blog.category.dto.CategoryResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping(path = "/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;
    private final CategoryMapper categoryMapper;

    @GetMapping
    public ResponseEntity<List<CategoryResponseDTO>> listCategories() {

        List<Category> categories = categoryService.listCategories();

        List<CategoryResponseDTO> categoryResponseDTOs = categories.stream()
                .map(categoryMapper::categoryToCategoryDTO)
                .toList();

        return ResponseEntity.ok(categoryResponseDTOs);
    }

    @PostMapping
    public ResponseEntity<CategoryResponseDTO> createCategory(
            @Valid @RequestBody CategoryRequestDTO categoryRequestDTO
    ) {
        log.info("creating category with body {}", categoryRequestDTO.toString());
        Category category = categoryMapper.categoryDTOtoCategory(categoryRequestDTO);
        Category createdCategory = categoryService.createCategory(category);

        CategoryResponseDTO categoryDTO = categoryMapper.categoryToCategoryDTO(createdCategory);

        return new ResponseEntity<>(categoryDTO, HttpStatus.CREATED);
    }
}
