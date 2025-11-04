package com.quiambao.facebook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Comparator;

@RestController
@RequestMapping("/api/posts")
// NOTE: The @CrossOrigin annotation is removed here 
// because global CORS is handled by WebConfig.java, which is the best practice.
public class FacebookController {

    @Autowired
    private FacebookRepository facebookRepository;

    /**
     * POST /api/posts - Creates a new post.
     * @param post The post details from the request body.
     * @return The saved post with HTTP status 201 (Created).
     */
    @PostMapping
    public ResponseEntity<FacebookPost> createPost(@RequestBody FacebookPost post) {
        // Validation: Ensure required fields are not empty
        if (post.getAuthor() == null || post.getAuthor().trim().isEmpty() ||
            post.getContent() == null || post.getContent().trim().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        
        // Ensure image URL is not null before saving (handles case where client sends empty string)
        if (post.getImageUrl() != null && post.getImageUrl().trim().isEmpty()) {
            post.setImageUrl(null);
        }
        
        FacebookPost savedPost = facebookRepository.save(post);
        return new ResponseEntity<>(savedPost, HttpStatus.CREATED);
    }

    /**
     * GET /api/posts - Retrieves all posts.
     * @return A list of all posts, sorted by creation date (newest first).
     */
    @GetMapping
    public List<FacebookPost> getAllPosts() {
        // Fetch all posts and sort them by ID or createdDateTime in descending order (newest first)
        List<FacebookPost> posts = facebookRepository.findAll();
        posts.sort(Comparator.comparing(FacebookPost::getCreatedDateTime).reversed());
        return posts;
    }

    /**
     * GET /api/posts/{id} - Retrieves a single post by ID.
     * @param id The ID of the post to retrieve.
     * @return The post with HTTP status 200, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<FacebookPost> getPostById(@PathVariable Long id) {
        return facebookRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * PUT /api/posts/{id} - Updates an existing post.
     * @param id The ID of the post to update.
     * @param postDetails The new post content and image URL.
     * @return The updated post with HTTP status 200, or 404 if not found.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FacebookPost> updatePost(@PathVariable Long id, @RequestBody FacebookPost postDetails) {
        return facebookRepository.findById(id)
            .map(post -> {
                // Check if content is provided for update
                if (postDetails.getContent() != null && !postDetails.getContent().trim().isEmpty()) {
                    post.setContent(postDetails.getContent());
                } else {
                    // Prevent empty content if required
                    return ResponseEntity.badRequest().build();
                }
                
                // Update image URL (allows setting to null/empty string)
                if (postDetails.getImageUrl() != null && postDetails.getImageUrl().trim().isEmpty()) {
                    post.setImageUrl(null);
                } else {
                    post.setImageUrl(postDetails.getImageUrl());
                }

                FacebookPost updatedPost = facebookRepository.save(post);
                return ResponseEntity.ok(updatedPost);
            })
            .orElse(ResponseEntity.notFound().build());
    }

    /**
     * DELETE /api/posts/{id} - Deletes a post by ID.
     * @param id The ID of the post to delete.
     * @return HTTP status 204 (No Content), or 404 if not found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        if (!facebookRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        facebookRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
