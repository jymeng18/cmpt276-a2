package com.example.cmpt276.a2.controller;

import com.example.cmpt276.a2.model.RoleType;
import com.example.cmpt276.a2.model.StaffRating;
import com.example.cmpt276.a2.repository.StaffRatingRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class StaffRatingControllersTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private StaffRatingRepository staffRatingRepo;

  private StaffRating testRating;

  @BeforeEach
  void setUp() {
    staffRatingRepo.deleteAll();
    testRating = new StaffRating(
        "Dr. Alice Johnson",
        "alice@university.edu",
        RoleType.PROF,
        8, 9, 10,
        "Excellent professor!"
    );
    testRating = staffRatingRepo.save(testRating);
  }

  @Test
  void testGetAllStaffRatings() throws Exception {
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attributeExists("ratings"))
        .andExpect(model().attributeExists("avgRatings"));
  }

  @Test
  void testGetAllStaffRatingsEmpty() throws Exception {
    staffRatingRepo.deleteAll();
    mockMvc.perform(get("/"))
        .andExpect(status().isOk())
        .andExpect(view().name("index"))
        .andExpect(model().attributeExists("ratings"));
  }

  @Test
  void testGetRatingForm() throws Exception {
    mockMvc.perform(get("/ratings/create"))
        .andExpect(status().isOk())
        .andExpect(view().name("form"));
  }

  @Test
  void testGetRatingDetail() throws Exception {
    mockMvc.perform(get("/ratings/" + testRating.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("detail"))
        .andExpect(model().attributeExists("rating"))
        .andExpect(model().attributeExists("avg"));
  }

  @Test
  void testGetRatingDetailNotFound() throws Exception {
    mockMvc.perform(get("/ratings/99999"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andExpect(flash().attributeExists("errorMessage"));
  }

  @Test
  void testGetEditForm() throws Exception {
    mockMvc.perform(get("/ratings/edit/" + testRating.getId()))
        .andExpect(status().isOk())
        .andExpect(view().name("edit"))
        .andExpect(model().attributeExists("previousRating"));
  }

  @Test
  void testGetEditFormNotFound() throws Exception {
    mockMvc.perform(get("/ratings/edit/99999"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"))
        .andExpect(flash().attributeExists("errorMessage"));
  }

  @Test
  void testCreateNewRating() throws Exception {
    mockMvc.perform(post("/ratings")
        .param("name", "Dr. Bob Smith")
        .param("email", "bob@university.edu")
        .param("roleType", "PROF")
        .param("clarity", "7")
        .param("niceness", "8")
        .param("knowledgeableScore", "9")
        .param("comment", "Very knowledgeable!"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));

    Optional<StaffRating> savedRating = staffRatingRepo.findByEmail("bob@university.edu");
    assertEquals("Dr. Bob Smith", savedRating.get().getName());
  }

  @Test
  void testCreateNewRatingWithInvalidEmail() throws Exception {
    mockMvc.perform(post("/ratings")
        .param("name", "Dr. Charly Brown")
        .param("email", "notanemail")
        .param("roleType", "TA")
        .param("clarity", "5")
        .param("niceness", "5")
        .param("knowledgeableScore", "5")
        .param("comment", "Test"))
        .andExpect(status().isOk())
        .andExpect(view().name("form"))
        .andExpect(model().attributeExists("errorMessage"));
        // Note: There are no redirections for invalid form submissions,
        // You are just returned the form template again
  }

  @Test
  void testCreateNewRatingWithDuplicateEmail() throws Exception {
    mockMvc.perform(post("/ratings")
        .param("name", "Another Person")
        .param("email", "alice@university.edu")
        .param("roleType", "TA")
        .param("clarity", "5")
        .param("niceness", "5")
        .param("knowledgeableScore", "5")
        .param("comment", "Test"))
        .andExpect(status().isOk())
        .andExpect(view().name("form"))
        .andExpect(model().attributeExists("errorMessage"));
        // Note: There are no redirections for invalid form submissions,
        // You are just returned the form template again
    
  }

  @Test
  void testCreateNewRatingWithBlankName() throws Exception {
    mockMvc.perform(post("/ratings")
        .param("name", "")
        .param("email", "test@test.com")
        .param("roleType", "STAFF")
        .param("clarity", "5")
        .param("niceness", "5")
        .param("knowledgeableScore", "5"))
        .andExpect(status().isOk())
        .andExpect(view().name("form"))
        .andExpect(model().attributeExists("errorMessage"));
  }

  @Test
  void testCreateNewRatingWithScoreOutOfRange() throws Exception {
    mockMvc.perform(post("/ratings")
        .param("name", "Test Person")
        .param("email", "valid@test.com")
        .param("roleType", "TA")
        .param("clarity", "11")
        .param("niceness", "5")
        .param("knowledgeableScore", "5"))
        .andExpect(status().isOk())
        .andExpect(view().name("form"))
        .andExpect(model().attributeExists("errorMessage"));
  }

  @Test
  void testCreateNewRatingWithScoreBelowRange() throws Exception {
    mockMvc.perform(post("/ratings")
        .param("name", "Test Person")
        .param("email", "valid@test.com")
        .param("roleType", "TA")
        .param("clarity", "5")
        .param("niceness", "0")
        .param("knowledgeableScore", "5"))
        .andExpect(status().isOk())
        .andExpect(view().name("form"))
        .andExpect(model().attributeExists("errorMessage"));
  }

  @Test
  void testEditRating() throws Exception {
    mockMvc.perform(post("/ratings/edit/" + testRating.getId())
        .param("name", "Dr. Alice Updated")
        .param("email", "alice@university.edu")
        .param("roleType", "PROF")
        .param("clarity", "10")
        .param("niceness", "10")
        .param("knowledgeableScore", "10")
        .param("comment", "Updated comment"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
    
    Optional<StaffRating> updated = staffRatingRepo.findById(testRating.getId());
    assertTrue(updated.isPresent());
  }

  @Test
  void testEditRatingWithInvalidData() throws Exception {
    mockMvc.perform(post("/ratings/edit/" + testRating.getId())
        .param("name", "Dr. Alice")
        .param("email", "alice@university.edu")
        .param("roleType", "PROF")
        .param("clarity", "15")
        .param("niceness", "8")
        .param("knowledgeableScore", "9"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/ratings/edit/" + testRating.getId()))
        .andExpect(flash().attributeExists("errorMessage"));
    
    Optional<StaffRating> unchanged = staffRatingRepo.findById(testRating.getId());
    assertTrue(unchanged.isPresent());
  }

  @Test
  void testEditRatingWithDuplicateEmail() throws Exception {
    StaffRating anotherRating = new StaffRating(
        "Bob Test", "bob@test.com", RoleType.TA, 5, 5, 5, "Test"
    );
    anotherRating = staffRatingRepo.save(anotherRating);
    
    mockMvc.perform(post("/ratings/edit/" + testRating.getId())
        .param("name", "Dr. Alice")
        .param("email", "bob@test.com")
        .param("roleType", "PROF")
        .param("clarity", "8")
        .param("niceness", "9")
        .param("knowledgeableScore", "10"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/ratings/edit/" + testRating.getId()))
        .andExpect(flash().attributeExists("errorMessage"));
  }

  @Test
  void testEditRatingKeepingSameEmail() throws Exception {
    mockMvc.perform(post("/ratings/edit/" + testRating.getId())
        .param("name", "Dr. Alice Updated")
        .param("email", "alice@university.edu")
        .param("roleType", "INSTRUCTOR")
        .param("clarity", "9")
        .param("niceness", "9")
        .param("knowledgeableScore", "9"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
    
    Optional<StaffRating> updated = staffRatingRepo.findById(testRating.getId());
    assertTrue(updated.isPresent());
  }

  @Test
  void testEditNonexistentRating() throws Exception {
    mockMvc.perform(post("/ratings/edit/99999")
        .param("name", "Test")
        .param("email", "test@test.com")
        .param("roleType", "TA")
        .param("clarity", "5")
        .param("niceness", "5")
        .param("knowledgeableScore", "5"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/ratings/edit/99999"))
        .andExpect(flash().attributeExists("errorMessage"));
  }

  @Test
  void testDeleteRating() throws Exception {
    long idToDelete = testRating.getId();
    
    mockMvc.perform(post("/ratings/delete/" + idToDelete))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/"));
    
    assertFalse(staffRatingRepo.findById(idToDelete).isPresent());
  }

  @Test
  void testDeleteNonexistentRating() throws Exception {
    mockMvc.perform(post("/ratings/delete/99999"))
        .andExpect(status().is3xxRedirection())
        .andExpect(redirectedUrl("/ratings/99999"))
        .andExpect(flash().attributeExists("errorMessage"));
  }
}
