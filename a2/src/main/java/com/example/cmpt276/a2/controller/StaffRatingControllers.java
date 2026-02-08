package com.example.cmpt276.a2.controller;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.cmpt276.a2.model.StaffRating;
import com.example.cmpt276.a2.repository.StaffRatingRepository;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@Controller
public class StaffRatingControllers {
  
  @Autowired
  private StaffRatingRepository staffRatingRepo;

  public StaffRatingControllers(StaffRatingRepository staffRatingRepo) {
    this.staffRatingRepo = staffRatingRepo;
  }

  @GetMapping("/")
  public String getAllStaffRatings(Model model){
    List<StaffRating> ratings = staffRatingRepo.findAll();
    System.out.println(ratings);
    model.addAttribute("ratings", ratings);

    ArrayList<Double> avgRatings = new ArrayList<>();
    for(StaffRating r : ratings){
      double clarity = r.getClarity();
      double niceness = r.getNiceness();
      double knowledgeableScore = r.getKnowledgeableScore();

      double avg = (clarity + niceness + knowledgeableScore) / 3.0;

      avgRatings.add(avg);
    }

    model.addAttribute("avgRatings", avgRatings);
    return "index";
  }

  @GetMapping("/ratings/create")
  public String createNewRating(Model model){
    return "form";
  }

  @PostMapping("/ratings")
  public String postMethodName(@ModelAttribute StaffRating rating, Model model) {

    // Insert data into db, then rerender
    staffRatingRepo.save(rating);
    List<StaffRating> ratings = staffRatingRepo.findAll();
    model.addAttribute("ratings", ratings);

    ArrayList<Double> avgRatings = new ArrayList<>();
    for(StaffRating r : ratings){
      double clarity = r.getClarity();
      double niceness = r.getNiceness();
      double knowledgeableScore = r.getKnowledgeableScore();

      double avg = (clarity + niceness + knowledgeableScore) / 3.0;

      avgRatings.add(avg);
    }

    model.addAttribute("avgRatings", avgRatings);
    return "index";
  }
    
  
}
