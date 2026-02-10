package com.example.cmpt276.a2.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class StaffRatingTest {

  private Validator validator;
  private StaffRating validRating;

  @BeforeEach
  void setUp() {
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
    validRating = new StaffRating("John Smith", "john@example.com", 
        RoleType.PROF, 8, 9, 7, "Great instructor!");
  }

  @Test
  void testValidStaffRating() {
    Set<ConstraintViolation<StaffRating>> violations = validator.validate(validRating);
    assertTrue(violations.isEmpty());
  }

  @Test
  void testInvalidEmail() {
    validRating.setEmail("notanemail");
    Set<ConstraintViolation<StaffRating>> violations = validator.validate(validRating);
    assertFalse(violations.isEmpty());
  }

  @Test
  void testBlankEmail() {
    validRating.setEmail("");
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testNullEmail() {
    validRating.setEmail(null);
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testBlankName() {
    validRating.setName("");
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testNullName() {
    validRating.setName(null);
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testScoreBelowMinimum() {
    validRating.setClarity(0);
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testScoreAboveMaximum() {
    validRating.setNiceness(11);
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testNullScore() {
    validRating.setKnowledgeableScore(null);
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testCommentTooLong() {
    validRating.setComment("a".repeat(401));
    assertFalse(validator.validate(validRating).isEmpty());
  }

  @Test
  void testNullComment() {
    validRating.setComment(null);
    assertTrue(validator.validate(validRating).isEmpty());
  }

  @Test
  void testBoundaryScoresMinimum() {
    validRating.setClarity(1);
    validRating.setNiceness(1);
    validRating.setKnowledgeableScore(1);
    assertTrue(validator.validate(validRating).isEmpty());
  }

  @Test
  void testBoundaryScoresMaximum() {
    validRating.setClarity(10);
    validRating.setNiceness(10);
    validRating.setKnowledgeableScore(10);
    assertTrue(validator.validate(validRating).isEmpty());
  }

  @Test
  void testGetProfile() {
    validRating.setRoleType(RoleType.PROF);
    StaffMemberProfile profProfile = validRating.getProfile();
    assertNotNull(profProfile);
    assertTrue(profProfile instanceof ProfessorProfile);
    assertEquals("Professor", profProfile.getDisplayTitle());
  }

  @Test
  void testGetProfileForTA() {
    validRating.setRoleType(RoleType.TA);
    StaffMemberProfile taProfile = validRating.getProfile();
    assertTrue(taProfile instanceof TAProfile);
    assertEquals("Teaching Assistant", taProfile.getDisplayTitle());
  }
}
