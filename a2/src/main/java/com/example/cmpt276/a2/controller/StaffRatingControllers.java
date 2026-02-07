package com.example.cmpt276.a2.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.example.cmpt276.a2.model.StaffRating;
import com.example.cmpt276.a2.repository.StaffRatingRepository;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;


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
    model.addAttribute("ratings", ratings);
    return "index";
  }

  @GetMapping("/ratings/create")
  public String createNewRating(Model model){
    return "newRating";
  }
  
  
}
