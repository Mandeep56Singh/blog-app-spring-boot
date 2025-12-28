package com.example.blog.post;

import com.example.blog.category.Category;
import com.example.blog.common.BaseEntity;
import com.example.blog.tag.Tag;
import com.example.blog.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "posts")
public class Post extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private PostStatus status;

    @Column(nullable = false)
    private Integer readingTime;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, name = "category_id")
    private Category category;

    @ManyToMany
    @JoinTable(
            name = "post_tags",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id")
    )

    private final Set<Tag> tags = new HashSet<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Post post = (Post) o;
        return Objects.equals(getId(), post.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + getId() +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", readingTime=" + readingTime +
                ", createdAt=" + getCreatedAt() +
                ", updatedAt=" + getUpdatedAt() +
                '}';
    }

    public Post(PostBuilder postBuilder) {
        this.title = postBuilder.title;
        this.content = postBuilder.content;
        this.status = postBuilder.status;
        this.readingTime = postBuilder.readingTime;
        this.category = postBuilder.category;
    }

    public static PostBuilder builder(String title, String content, PostStatus status, Integer readingTime, Category category) {
        return new PostBuilder(title, content, status, readingTime, category);
    }

    public static class PostBuilder {
        private String title;
        private String content;
        private PostStatus status;
        private Integer readingTime;
        private Category category;

        public PostBuilder readingTime(Integer readingTime) {
            this.readingTime = readingTime;
            return this;
        }

        public PostBuilder status(PostStatus status) {
            this.status = status;
            return this;
        }

        public PostBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostBuilder category(Category category) {
            this.category = category;
            return this;
        }

        public PostBuilder(String title, String content, PostStatus status, Integer readingTime, Category category) {
            this.title = title;
            this.content = content;
            this.status = status;
            this.readingTime = readingTime;
            this.category = category;
        }

        public Post build() {
            return new Post(this);
        }
    }


}