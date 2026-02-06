package com.example.cmpt276.a2.model;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff_ratings")
public class StaffRating {
  public StaffRating(){}

  public StaffRating(String name, String email, RoleType roleType, 
    Integer clarity, Integer niceness, Integer knowledgeableScore, String comment) {
    this.name = name;
    this.email = email;
    this.roleType = roleType;
    this.clarity = clarity;
    this.niceness = niceness;
    this.knowledgeableScore = knowledgeableScore;
    this.comment = comment;
  }
  
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @NotBlank(message = "Name is required.")
  @Size(min = 1, max = 200)
  private String name;

  @NotBlank(message = "Email is required.")
  @Email(message = "Valid email is required.")
  @Column(unique = true)
  private String email;

  @NotNull
  @Enumerated(EnumType.STRING) 
  private RoleType roleType;

  @Min(value = 1, message = "Val must be at least 1.")
  @Max(value = 10, message = "Max val is 10.")
  private Integer clarity;

  @Min(value = 1, message = "Val must be at least 1.")
  @Max(value = 10, message = "Max val is 10.")
  private Integer niceness;

  @Min(value = 1, message = "Val must be at least 1.")
  @Max(value = 10, message = "Max val is 10.")
  private Integer knowledgeableScore;

  @Size(min = 1, max = 400)
  private String comment;

  @CreationTimestamp
  @Column(nullable = false, updatable = false)
  private LocalDateTime createdAt;
  
  @UpdateTimestamp
  @Column(nullable = false, updatable = true)
  private LocalDateTime updatedAt;

  // Getters & Setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public RoleType getRoleType() {
    return roleType;
  }

  public void setRoleType(RoleType roleType) {
    this.roleType = roleType;
  }

  public Integer getClarity() {
    return clarity;
  }

  public void setClarity(Integer clarity) {
    this.clarity = clarity;
  }

  public Integer getNiceness() {
    return niceness;
  }

  public void setNiceness(Integer niceness) {
    this.niceness = niceness;
  }

  public Integer getKnowledgeableScore() {
    return knowledgeableScore;
  }

  public void setKnowledgeableScore(Integer knowledgeableScore) {
    this.knowledgeableScore = knowledgeableScore;
  }

  public String getComment() {
    return comment;
  }

  public void setComment(String comment) {
    this.comment = comment;
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(LocalDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public LocalDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(LocalDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

}
