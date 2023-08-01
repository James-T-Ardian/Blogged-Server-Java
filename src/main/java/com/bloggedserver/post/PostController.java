package com.bloggedserver.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts")
@Validated
public class PostController {

    private final PostService postService;

    @GetMapping("/")
    public ResponseEntity<List<Post>> getPosts() {
        List<Post> posts = postService.getPosts();
        return ResponseEntity.ok(posts);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus (HttpStatus.BAD_REQUEST)
    public void handleInvalidRequestBody() {

    }

    @ExceptionHandler({DataAccessException.class})
    @ResponseStatus (HttpStatus.INTERNAL_SERVER_ERROR)
    public void handleJDBCDataAccessFailure() {

    }

    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void createPost(@RequestBody @Valid CreatePostRequest request) {
        postService.createPost(request.getTitle(), request.getBody(), request.getCreated_at());
    }

    @GetMapping("/{postID}")
    public ResponseEntity<Post> getPostById(@PathVariable(value = "postID") int postID) {
        Optional<Post> post = postService.getPostById(postID);
        if (post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }

    @PutMapping("/{postID}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@PathVariable(value = "postID") int postID, @Validated @RequestBody UpdatePostRequest request) {
        postService.updatePost(request.getTitle(), request.getBody(), postID);
    }

    @DeleteMapping ("/{postID}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@PathVariable(value = "postID") int postID) {
        postService.deletePost(postID);
    }
}
