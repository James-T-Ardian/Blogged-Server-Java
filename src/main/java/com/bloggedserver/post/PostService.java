package com.bloggedserver.post;

import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

@Service
public class PostService {

    private final PostJdbcTemplateRepositoryImpl PostJdbcTemplateRepository;

    @Autowired
    public PostService(PostJdbcTemplateRepositoryImpl postJdbcTemplateRepositoryImpl) {
        this.PostJdbcTemplateRepository = postJdbcTemplateRepositoryImpl;
    }

    public List<Post> getPosts() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        return PostJdbcTemplateRepository.findAllByUsername(authenticatedUser);
    }

    public Optional<Post> getPostById(int postId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        Optional<Post> post = PostJdbcTemplateRepository.findById(postId);
        if (post.isPresent() && post.get()
                .uploader()
                .equals(authenticatedUser)) {
            return post;
        } else {
            return Optional.empty();
        }
    }

    public Post createPost(String title, String body, String createdAt) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        Post newPost = new Post(OptionalInt.empty(), title, body, Optional.of(createdAt), authenticatedUser);
        return PostJdbcTemplateRepository.save(newPost);
    }

    public Post updatePost(@NotNull String title, @NotNull String body, int postId, @NotNull String uploaderUsername) {
        Post updatedPost = new Post(OptionalInt.of(postId), title, body, Optional.empty(), uploaderUsername);
        return PostJdbcTemplateRepository.save(updatedPost);
    }

    public void deletePost(int postId) {
        PostJdbcTemplateRepository.deleteById(postId);
    }
}
