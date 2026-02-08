package com.example.cmpt276.a2.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;

import com.example.cmpt276.a2.model.StaffRating;
import com.example.cmpt276.a2.repository.StaffRatingRepository;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

import jakarta.validation.Valid;
import java.util.Optional;

@Controller
public class StaffRatingControllers {

    @Autowired
    private StaffRatingRepository staffRatingRepo;

    public StaffRatingControllers(StaffRatingRepository staffRatingRepo) {
        this.staffRatingRepo = staffRatingRepo;
    }

    @GetMapping("/")
    public String getAllStaffRatings(Model model) {
        List<StaffRating> ratings = staffRatingRepo.findAll();
        model.addAttribute("ratings", ratings);

        ArrayList<Double> avgRatings = new ArrayList<>();
        for (StaffRating r : ratings) {
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
    public String getRatingForm(Model model) {
        return "form";
    }

    @PostMapping("/ratings")
    public String createNewRating(@Valid @ModelAttribute StaffRating rating, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
            return "form";
        }

        // Check email uniqueness
        if (staffRatingRepo.findByEmail(rating.getEmail()).isPresent()) {
            model.addAttribute("errorMessage", "Email already exists.");
            return "form";
        }

        staffRatingRepo.save(rating);
        List<StaffRating> ratings = staffRatingRepo.findAll();
        model.addAttribute("ratings", ratings);

        ArrayList<Double> avgRatings = new ArrayList<>();
        for (StaffRating r : ratings) {
            double clarity = r.getClarity();
            double niceness = r.getNiceness();
            double knowledgeableScore = r.getKnowledgeableScore();

            double avg = (clarity + niceness + knowledgeableScore) / 3.0;
            avgRatings.add(avg);
        }

        model.addAttribute("avgRatings", avgRatings);
        return "index";
    }

    // View in detail
    @GetMapping("/ratings/{id}")
    public String getRatingDetail(@PathVariable("id") Long id, Model model) {
        Optional<StaffRating> detailedRating = staffRatingRepo.findById(id);

        if (detailedRating.isEmpty()) {
            model.addAttribute("errorMessage", "Staff rating not found.");
            return "index";
        }

        StaffRating rating = detailedRating.get();
        double avg = (rating.getClarity() + rating.getNiceness() + rating.getKnowledgeableScore()) / 3.0;

        model.addAttribute("rating", rating);
        model.addAttribute("avg", avg);

        return "detail";
    }

    // Display edit form
    @GetMapping("/ratings/edit/{id}")
    public String getEditForm(@PathVariable("id") Long id, Model model) {
        Optional<StaffRating> rating = staffRatingRepo.findById(id);

        if (rating.isEmpty()) {
            model.addAttribute("errorMessage", "Staff Rating Entry not found!");
            return "index";
        }
        model.addAttribute(rating.get());
        return "edit";
    }

}
