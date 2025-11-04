package com.quiambao.facebook;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "facebook_posts")
public class FacebookPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String author;

    @Lob // For potentially long content
    @Column(nullable = false)
    private String content;

    private String imageUrl; // Optional

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDateTime;

    @Column(nullable = false)
    private LocalDateTime modifiedDateTime;

    // --- Automatic Timestamp Management ---
    @PrePersist
    protected void onCreate() {
        this.createdDateTime = LocalDateTime.now();
        this.modifiedDateTime = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.modifiedDateTime = LocalDateTime.now();
    }

    // --- Standard Getters and Setters (REQUIRED) ---
    // (You must include these for Spring/JPA to work)

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public LocalDateTime getCreatedDateTime() { return createdDateTime; }
    // No setter for createdDateTime as it's set in @PrePersist

    public LocalDateTime getModifiedDateTime() { return modifiedDateTime; }
    // No setter for modifiedDateTime as it's set in @PreUpdate
}