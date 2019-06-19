package org.launchcode.controllers;

import org.launchcode.models.*;
import org.launchcode.models.forms.JobForm;
import org.launchcode.models.data.JobData;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Created by LaunchCode
 */
@Controller
@RequestMapping(value = "job")
public class JobController {

    private JobData jobData = JobData.getInstance();

    // The detail display for a given Job at URLs like /job?id=17
    @RequestMapping(value = "", method = RequestMethod.GET)
    public String index(Model model, @RequestParam("id") int id) {

        // TODO #1 - get the Job with the given ID and pass it into the view
        JobData data = JobData.getInstance();
        Job givenJob = data.findById(id);

        model.addAttribute("givenJob", givenJob);

        return "job-detail";
    }

    @RequestMapping(value = "add", method = RequestMethod.GET)
    public String add(Model model) {
        model.addAttribute(new JobForm());
        return "new-job";
    }

    @RequestMapping(value = "add", method = RequestMethod.POST)
    public String add(@ModelAttribute @Valid JobForm jobForm, Errors errors, Model model, RedirectAttributes redirectAttributes) {

        // TODO #6 - Validate the JobForm model, and if valid, create a
        // new Job and add it to the jobData data store. Then
        // redirect to the job detail view for the new Job.

        if (errors.hasErrors()) {
            return "new-job";
        } else {

            String name = jobForm.getName();
            Employer employer = jobData.getEmployers().findById(jobForm.getEmployerId());
            Location location = jobData.getLocations().findById(jobForm.getLocationId());
            PositionType positionType = jobData.getPositionTypes().findById(jobForm.getPositionTypeId());
            CoreCompetency coreCompetency = jobData.getCoreCompetencies().findById(jobForm.getCoreCompetencyId());


            Job newJob = new Job(name, employer, location, positionType, coreCompetency);
            jobData.add(newJob);

            int id = newJob.getId();

            redirectAttributes.addAttribute("id", id);

            return "redirect:/job";
        }
    }
}
