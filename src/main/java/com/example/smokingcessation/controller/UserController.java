package com.example.smokingcessation.controller;


import com.example.smokingcessation.model.SubModels;
import com.example.smokingcessation.model.User;
import com.example.smokingcessation.repo.UserRepository;
import com.example.smokingcessation.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.CrossOrigin;
@CrossOrigin
@RestController
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/checkSession/{sessionID}")
    public ResponseEntity<Boolean> checkSession(@PathVariable("sessionID") String sessionID){
        return new ResponseEntity<>(userService.checkSession(sessionID), HttpStatus.OK);
    }

    @GetMapping("/amountAddedPerSecond/{id}")
    public ResponseEntity<Double> getAmountAddedPerSecond(@PathVariable("id") long id){
        return new ResponseEntity<>(userService.getAmountAddedPerSecond(id), HttpStatus.OK);
    }
    @GetMapping("/stoppedSmokingDate/{sessionID}")
    public ResponseEntity<LocalDateTime> getStoppedSmokingDate(@PathVariable("sessionID")String sessionID){
        System.out.println(sessionID);
        return new ResponseEntity<>(userService.getStoppedSmokingDate(sessionID), HttpStatus.OK);
    }

    @GetMapping("/profileInfo/{sessionID}")
    public ResponseEntity<SubModels.UserProfile> getProfileInfo(@PathVariable("sessionID") String sessionID){
        return new ResponseEntity<>(userService.getProfileInfo(sessionID), HttpStatus.OK);
    }

    @GetMapping(
            value = "/userProfile/{sessionID}",
            produces = MediaType.IMAGE_PNG_VALUE)
    public @ResponseBody byte[] getProfilePicture(@PathVariable("sessionID") String sessionID) throws IOException {
        byte[] userProfile = userService.getUserProfile(sessionID);
        return userProfile;
    }

//    @PostMapping(
//            value = "/login",
//            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
//    public ResponseEntity<SubModels.ResponseUser> login(@RequestBody SubModels.Login loginData){
//        SubModels.ResponseUser user = userService.login(loginData.getUsername(), loginData.getPassword());
//        return new ResponseEntity<>(user,user==null?HttpStatus.NO_CONTENT:HttpStatus.OK);
//    }

    @PostMapping(
            value = "/post",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Boolean> post(@RequestBody SubModels.SentPost sentPost){
        userService.post(sentPost);
        return new ResponseEntity<>(true,HttpStatus.OK);
    }


    @PostMapping(
            value = "/setStoppedSmokingDate",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<LocalDateTime> setStoppedSmokingDate(@RequestBody SubModels.StoppedSmokingDate stoppedSmokingDate){
        System.out.println("Received Post request");
        LocalDateTime newDateTime = userService.setStoppedSmokingDate(stoppedSmokingDate.getSessionID(),stoppedSmokingDate.getStoppedSmokingDate());
        return new ResponseEntity<>(newDateTime,HttpStatus.OK);
    }

    @PostMapping(
            value = "/smokingData",
            consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ResponseEntity<Double> setAmountAddedPerSecond(@RequestBody SubModels.SmokingData smokingData){
        System.out.println(smokingData);
        Double newAmountAddedPerSecond = userService.saveSmokingData(
                smokingData.getSessionID(),
                smokingData.getCigarette().getPrice(),
                smokingData.getTimesADay());
        return new ResponseEntity<>(newAmountAddedPerSecond, HttpStatus.OK);
    }

    @GetMapping("/savedMoneyData/{sessionID}")
    public ResponseEntity<SubModels.SavedMoneyData> getSavedMoneyDate(@PathVariable("sessionID")String sessionID){
        return new ResponseEntity<>(userService.getSavedMoneyData(sessionID), HttpStatus.OK);
    }

    @GetMapping("/dailyTask/{sessionID}")
    public ResponseEntity<String> getLastShowDailyChallenges(@PathVariable("sessionID") String sessionID){
        LocalDate lastShowDailyChallenge = userService.getLastShowDailyTask(sessionID);
        return new ResponseEntity<>(lastShowDailyChallenge.toString(),HttpStatus.OK);
    }

    @GetMapping("/startDate/{id}")
    public ResponseEntity<String> getStartingDate(@PathVariable("id") Long id){
        LocalDateTime startingDate = userService.getStartingDate(id);
        return new ResponseEntity<>(startingDate.toString(),HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<User> updateUser(@RequestBody User user){
        System.out.println("RECEIVED PUT REQUEST");
        userService.saveUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/timesSmokedSinceStart/{id}")
    public ResponseEntity<Integer> getTimesSmokedSinceStart(@PathVariable("id") Long id){
        return new ResponseEntity<>(userService.getTimesSmokedSinceStart(id), HttpStatus.OK);
    }

    @PostMapping("/removeSession")
    public void removeSession(@RequestBody String sessionID){
        System.out.println(sessionID);
        userService.removeSession(sessionID);
    }

}
