package com.bloggedserver.post;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;

// Note: refer to constraints set in Post model to get an idea of the method parameter constraints in this class
// Temporary note (to be deleted later): service is where authentication logic will be in

@Service
public class PostService {

    private final PostJdbcTemplateRepositoryImpl PostJdbcTemplateRepositoryImpl;

    @Autowired
    public PostService(PostJdbcTemplateRepositoryImpl postJdbcTemplateRepositoryImpl) {
        this.PostJdbcTemplateRepositoryImpl = postJdbcTemplateRepositoryImpl;
    }

    public List<Post> getPostByUsername(@NotNull String uploaderUsername) {
        return PostJdbcTemplateRepositoryImpl.findAllByUsername(uploaderUsername);
    }

    public Optional<Post> getPostById(int postId) {
        return PostJdbcTemplateRepositoryImpl.findById(postId);
    }

    public Post createPost(@NotNull String title, @NotNull String body, @NotNull @Pattern(regexp = "^((19|20)" +
            "\\\\d\\\\d)-" + "(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$") String createdAt,
                           @NotNull String uploaderUsername) {
        Post newPost = new Post(OptionalInt.empty(), title, body, Optional.of(createdAt), uploaderUsername);
        return PostJdbcTemplateRepositoryImpl.save(newPost);
    }

    public Post updatePost(@NotNull String title, @NotNull String body, int postId, @NotNull String uploaderUsername) {
        Post updatedPost = new Post(OptionalInt.of(postId), title, body, Optional.empty(), uploaderUsername);
        return PostJdbcTemplateRepositoryImpl.save(updatedPost);
    }

    public void deletePost(int postId) {
        PostJdbcTemplateRepositoryImpl.deleteById(postId);
    }
}
