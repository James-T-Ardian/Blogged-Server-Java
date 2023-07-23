package com.bloggedserver.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @GetMapping("/{postID}")
    public ResponseEntity<Post> getPostById(@PathVariable(value="postID") int postID) {
        Optional<Post> post = postService.getPostById(postID);
        if(post.isPresent()) {
            return ResponseEntity.ok(post.get());
        } else {
            return ResponseEntity.notFound()
                    .build();
        }
    }
}
