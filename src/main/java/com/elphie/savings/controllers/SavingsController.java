package com.elphie.savings.controllers;

// =============================================================================
// File Name: controllers/${TM_FILENAME_BASE}.java
// File Description:
// This file contains the code of the ${TM_FILENAME_BASE} that handles
// the Http requests, manipulates the data and returns the responses.
// =============================================================================

// =============================================================================
// Controller Imports
// =============================================================================
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elphie.savings.libs.Utiles;
import com.elphie.savings.models.Savings;
import com.elphie.savings.repositories.ISavingsRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

// =============================================================================
// Controller Class
// =============================================================================
@CrossOrigin(origins = "http://localhost:8000")
@RestController
@RequestMapping("/savings/")
public class SavingsController {
    
    // PROPERTIES ////////////////
    @Autowired
    private ISavingsRepository savingsRepository;

    // HTTP REQUEST METHODS ////////////////

    /**
     * Used to CREATE a savings and add it to the DB.
     * Strategy: Validate data coming from FE, Try add to DB and Catch Errors.
     * Steps: 
     *    1 -> If validation has ERRORS return ERROR Response with 400 Bad Request Status with ERRORS Array
     *    3 -> Try add to DB
     *    4 -> If added to DB then return SUCCESS Response with 200 Ok status with created Object
     *    5 -> Catch Server side errors -> If any then return ERRO Response with 500 Internal Server Error with Message.
     * @param savings type Savings from Request Body
     * @return ResponseEntity<Object> -> either SUCCESS Response 200 ok | ERROR Response 400 Bad Request | ERROR 500 Internal Server Error
     */
    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Savings savings) {
        
        // Get ERRORS ArrayList
        ArrayList<String> errors = Utiles.validateSavings(savings);

        // Return ERROR Response 400 Bad Request
        if(errors != null && errors.size() > 0) {
            return Utiles.generateResponse(HttpStatus.BAD_REQUEST, HttpStatus.BAD_REQUEST.getReasonPhrase(), errors);
        }

        try {
            // Set User created_on Timestamp as current time.
            savings.setCreatedOn(new Timestamp(System.currentTimeMillis()));

            // Add user to DB
            Savings createdSavings = savingsRepository.save(savings);

            // Return SUCCESS Response 200 OK
            return Utiles.generateResponse(HttpStatus.OK, "Success creating Savings.", createdSavings);

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,  error.getMessage(), "Failed to add Savings to DB.");
        }
    }


    @GetMapping(value="/get", params={"userId", "name", "rowsPerPage"})
    public ResponseEntity<Object> getPagesCount(
        @RequestParam(name="userId") long userId,
        @RequestParam(name="name") String query, 
        @RequestParam(name="rowsPerPage") String rows
    ) {
        try {
            // Find or Throw Exception
            int count = savingsRepository.countByUserIdAndNameContaining(userId, query);

            int totalPages = (int) count / Integer.parseInt(rows);
            int exces = (int)count % Integer.parseInt(rows);
            
            // Return SUCCESS Response 200 OK
            return Utiles.generateResponse(HttpStatus.OK, "Success getting Pages.", exces > 0 ? totalPages + 1 : totalPages); 

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,  error.getMessage(), "Failed to get Savings Pages to DB.");
        }
    }

    @GetMapping(value="/get", params={"userId", "name"})
    public ResponseEntity<Object> getFilteredSavings(
        @RequestParam(name="userId") long userId,
        @RequestParam(name="name") String query
    ) {
        try {
            // Find or Throw Exception
            // List<Savings> filteredSavings = savingsRepository.findByNameContainingWithLimitAndOffset(userId, query, limit, offset);
            List<Savings> filteredSavings = savingsRepository.findByUserIdAndNameContaining(userId, query);

            // Return SUCCESS Response 200 OK
            return Utiles.generateResponse(HttpStatus.OK, "Success getting Pages.", filteredSavings); 

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(HttpStatus.INTERNAL_SERVER_ERROR,  error.getMessage(), "Failed to get Savings Pages to DB.");
        }
    }
    
    
}
