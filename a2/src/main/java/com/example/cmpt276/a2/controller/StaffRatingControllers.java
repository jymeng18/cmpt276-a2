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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import java.util.Optional;
import org.springframework.web.bind.annotation.RequestBody;


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
        return "redirect:/";
    }

    // View in detail
    @GetMapping("/ratings/{id}")
    public String getRatingDetail(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<StaffRating> detailedRating = staffRatingRepo.findById(id);

        if (detailedRating.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Staff rating not found.");
            return "redirect:/";
        }

        StaffRating rating = detailedRating.get();
        double avg = (rating.getClarity() + rating.getNiceness() + rating.getKnowledgeableScore()) / 3.0;

        model.addAttribute("rating", rating);
        model.addAttribute("avg", avg);

        return "detail";
    }

    // Display edit form
    @GetMapping("/ratings/edit/{id}")
    public String getEditForm(@PathVariable("id") Long id, Model model, RedirectAttributes redirectAttributes) {
        Optional<StaffRating> rating = staffRatingRepo.findById(id);

        if (rating.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Staff Rating Entry not found!");
            return "redirect:/";
        }
        model.addAttribute("previousRating", rating.get());
        return "edit";
    }

    // User edit submission
    @PostMapping("/ratings/edit/{id}")
    public String putMethodName(@PathVariable("id") Long id, @Valid @ModelAttribute StaffRating editedRating, 
    BindingResult bindingResult, RedirectAttributes redirectAttributes) {
      
      if(bindingResult.hasErrors()){
        redirectAttributes.addFlashAttribute("errorMessage", bindingResult.getFieldError().getDefaultMessage());
        return "redirect:/ratings/edit/" + id;
      }

      if (staffRatingRepo.findByEmail(editedRating.getEmail()).isPresent() && editedRating.getId() != staffRatingRepo.findById(id).get().getId()) {
        redirectAttributes.addFlashAttribute("errorMessage", "Email already exists.");
        return "redirect:/ratings/edit/" + id;
      }

      // Update previous entry in db
      if(staffRatingRepo.existsById(id)){
        editedRating.setId(id);
        staffRatingRepo.save(editedRating);
      } else {
        redirectAttributes.addFlashAttribute("errorMessage", "Edited entry does not exist.");
        return "redirect:/ratings/edit/" + id;
      }

      return "redirect:/";
    }

    @PostMapping("/ratings/delete/{id}")
    public String deleteRating(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
        if(!staffRatingRepo.findById(id).isPresent()){
            redirectAttributes.addFlashAttribute("errorMessage", "Entry does not exist.");
            return "redirect:/ratings/" + id;
        }

        staffRatingRepo.deleteById(id);
        return "redirect:/";
    }
    

}
