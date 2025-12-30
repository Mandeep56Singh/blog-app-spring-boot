package com.example.blog.category;

import com.example.blog.category.dto.CategoryRequestDTO;
import com.example.blog.category.dto.CategoryResponseDTO;
import com.example.blog.post.Post;
import com.example.blog.post.PostStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface CategoryMapper {

    @Mapping(target = "postCount", source = "posts", qualifiedByName = "calculatePostCount")
    CategoryResponseDTO categoryToCategoryDTO(Category category);

    Category categoryDTOtoCategory(CategoryRequestDTO categoryRequestDTO);

    @Named("calculatePostCount")
    default long calculatePostCount(List<Post> posts) {

        if (posts == null || posts.isEmpty()) {
            return 0;
        }

        return posts.stream().filter(post -> post.getStatus().equals(PostStatus.PUBLISHED)).count();
    }
}
