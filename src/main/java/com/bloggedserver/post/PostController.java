package com.bloggedserver.post;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
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

    //Todo: Create exception controller for invalid method argument so that returned response status will be 400
    @PostMapping("/")
    @ResponseStatus(HttpStatus.OK)
    public void createPost(@RequestBody @Valid CreatePostRequest request) {
        Post posts = postService.createPost(request.getTitle(), request.getBody(), request.getCreated_at());
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

    //Todo: Create controller for updating posts and change service methods as necessary

    //Todo: Create controller for deleting posts and change service methods as necessary
}
