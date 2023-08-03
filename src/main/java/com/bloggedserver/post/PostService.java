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
        return PostJdbcTemplateRepository.findAll(authenticatedUser);
    }

    public Optional<Post> getPostById(int postId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        Optional<Post> post = PostJdbcTemplateRepository.findById(postId, authenticatedUser);
        return post;
    }

    public void createPost(String title, String body, String createdAt) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        Post newPost = new Post(OptionalInt.empty(), title, body, Optional.of(createdAt), authenticatedUser);
        PostJdbcTemplateRepository.save(newPost);
    }

    public void updatePost(String title, String body, int postId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        Post updatedPost = new Post(OptionalInt.of(postId), title, body, Optional.empty(), authenticatedUser);
        PostJdbcTemplateRepository.save(updatedPost);
    }

    public void deletePost(int postId) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        PostJdbcTemplateRepository.delete(postId, authenticatedUser);
    }
}
