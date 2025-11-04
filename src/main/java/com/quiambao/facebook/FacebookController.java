package com.quiambao.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class FacebookController {

    @Autowired
    private FacebookRepository facebookRepository;

    // POST: /api/posts - CREATE Post
    @PostMapping
    public ResponseEntity<FacebookPost> createPost(@RequestBody FacebookPost post) {
        // @PrePersist sets the initial timestamps before saving
        FacebookPost savedPost = facebookRepository.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    // GET: /api/posts - READ All Posts
    @GetMapping
    public List<FacebookPost> getAllPosts() {
        return facebookRepository.findAll();
    }

    // GET: /api/posts/{id} - READ Single Post
    @GetMapping("/{id}")
    public ResponseEntity<FacebookPost> getPostById(@PathVariable Long id) {
        return facebookRepository.findById(id)
                .map(ResponseEntity::ok) // Return 200 OK with the post
                .orElse(ResponseEntity.notFound().build()); // Return 404 Not Found
    }

    // PUT: /api/posts/{id} - UPDATE Post
    @PutMapping("/{id}")
    public ResponseEntity<FacebookPost> updatePost(@PathVariable Long id, @RequestBody FacebookPost postDetails) {
        return facebookRepository.findById(id)
                .map(post -> {
                    // Update allowed fields
                    post.setContent(postDetails.getContent());
                    post.setImageUrl(postDetails.getImageUrl());
                    // @PreUpdate handles the modifiedDateTime automatically

                    FacebookPost updatedPost = facebookRepository.save(post);
                    return ResponseEntity.ok(updatedPost);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE: /api/posts/{id} - DELETE Post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (!facebookRepository.existsById(id)) {
            return ResponseEntity.notFound().build(); // Return 404 if post doesn't exist
        }
        facebookRepository.deleteById(id);
        return ResponseEntity.noContent().build(); // Return 204 No Content for successful deletion
    }
}