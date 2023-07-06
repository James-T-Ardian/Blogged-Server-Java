package com.bloggedserver.service;

import com.bloggedserver.model.Post;
import com.bloggedserver.repository.PostJdbcTemplateRepository;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

// Note: refer to constraints set in Post model to get an idea of the method parameter constraints in this class
// Temporary note (to be deleted later): service is where authentication logic will be in

@Service
public class PostService {

    private final PostJdbcTemplateRepository postJdbcTemplateRepository;

    @Autowired
    public PostService(PostJdbcTemplateRepository postJdbcTemplateRepository) {
        this.postJdbcTemplateRepository = postJdbcTemplateRepository;
    }

    public List<Post> getPostByUsername(@NotNull String uploaderUsername) {
        return postJdbcTemplateRepository.getByUsername(uploaderUsername);
    }

    public List<Post> getPostById(int postId) {
        return postJdbcTemplateRepository.getById(postId);
    }

    public int createPost(@NotNull String title, @NotNull String body, @NotNull @Pattern(regexp = "^((19|20)" +
            "\\\\d\\\\d)-" + "(0?[1-9]|1[012])-(0?[1-9]|[12][0-9]|3[01])$") String createdAt,
                          @NotNull String uploaderUsername) {
        return postJdbcTemplateRepository.create(title, body, createdAt, uploaderUsername);
    }

    public int updatePost(@NotNull String title, @NotNull String body, int postId) {
        return postJdbcTemplateRepository.update(title, body, postId);
    }

    public int deletePost(int postId) {
        return postJdbcTemplateRepository.delete(postId);
    }
}
