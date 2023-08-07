package com.bloggedserver.post;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.*;

// Get functions allow you to get other user's posts. But users cannot update/delete other's posts nor
// create posts under a different user
@Service
public class PostService {

    private final PostJdbcTemplateRepositoryImpl PostJdbcTemplateRepository;

    @Autowired
    public PostService(PostJdbcTemplateRepositoryImpl postJdbcTemplateRepositoryImpl) {
        this.PostJdbcTemplateRepository = postJdbcTemplateRepositoryImpl;
    }

    public boolean doesAuthenticatedUserOwnPost(Post post) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        return post.uploader().equals(authenticatedUser);
    }

    public GetPostResponse getPostsByUsername(Optional<String> username) {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();
        String authenticatedUser = authentication.getName();
        List<Post> postList;
        boolean isOwner;
        if (username.isPresent()) {
            postList = PostJdbcTemplateRepository.findAllByUsername(username.get());
            isOwner = username.get().equals(authenticatedUser);
        } else {
            postList = PostJdbcTemplateRepository.findAllByUsername(authenticatedUser);
            isOwner = true;
        }

        GetPostResponse response = GetPostResponse.builder()
                .posts(postList)
                .isOwner(isOwner)
                .build();
        return response;
    }

    public GetPostResponse getPostById(int postId) {
        Optional<Post> post = PostJdbcTemplateRepository.findById(postId);
        List<Post> postList = new ArrayList<Post>();
        GetPostResponse response;
        if (post.isPresent()) {
            postList.add(post.get());
            response = GetPostResponse.builder()
                    .posts(postList)
                    .isOwner(doesAuthenticatedUserOwnPost(postList.get(0)))
                    .build();
        } else {
            response = GetPostResponse.builder()
                    .posts(postList)
                    .isOwner(false)
                    .build();
        }
        return response;
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
        PostJdbcTemplateRepository.deleteByIdAndUsername(postId, authenticatedUser);
    }
}
