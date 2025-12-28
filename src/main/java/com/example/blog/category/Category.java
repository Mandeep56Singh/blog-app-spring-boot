package com.example.blog.category;

import com.example.blog.common.BaseEntity;
import com.example.blog.post.Post;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "categories")
public class Category extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category")
    private final List<Post> posts = new ArrayList<>();

    private Category(CategoryBuilder categoryBuilder) {
        this.name = categoryBuilder.name;
    }

    public static CategoryBuilder builder(String name) {
        return new CategoryBuilder(name);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(getId(), category.getId());
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + getId() +
                ", name='" + name + '\'' +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public static class CategoryBuilder {
        private String name;

        public CategoryBuilder(String name) {
            this.name = name;
        }

        public CategoryBuilder name(String name) {
            this.name = name;
            return this;
        }

        public Category build() {
            return new Category(this);
        }
    }


}