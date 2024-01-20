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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.List;

import org.hibernate.query.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.elphie.savings.descriptions.SavingsRequest;
import com.elphie.savings.libs.Utiles;
import com.elphie.savings.models.Savings;
import com.elphie.savings.repositories.ISavingsRepository;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
     *    2 -> Try add to DB
     *    3 -> If added to DB then return SUCCESS Response with 200 Ok status with created Object
     *    4 -> Catch Server side errors -> If any then return ERRO Response with 500 Internal Server Error with Message.
     * @param savings type Savings
     * @return ResponseEntity<Object> -> either SUCCESS Response 200 ok | ERROR Response 400 Bad Request | ERROR 500 Internal Server Error
     */
    @PostMapping("/create")
    public ResponseEntity<Object> create(@RequestBody Savings savings) {
        
        // Get ERRORS ArrayList
        ArrayList<String> errors = Utiles.validateSavings(savings);

        // Return ERROR Response 400 Bad Request
        if(errors != null && errors.size() > 0) {
            return Utiles.generateResponse(
                HttpStatus.BAD_REQUEST, 
                HttpStatus.BAD_REQUEST.
                getReasonPhrase(), 
                errors);
        }

        try {
            // Set Savings created_on Timestamp as current time.
            savings.setCreatedOn(new Timestamp(System.currentTimeMillis()));

            // Add Savings to DB
            Savings createdSavings = savingsRepository.save(savings);

            // Return SUCCESS Response 200 OK
            return Utiles.generateResponse(
                HttpStatus.OK, 
                "Success creating Savings.", 
                createdSavings
            );

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,  
                error.getMessage(), 
                "Failed to add Savings to DB."
            );
        }
    }

    /**
     * Used to GET Savings by ID from the DB.
     * Strategy: Validate data coming from FE, Try find Savings in DB, Catch errors if any.
     * Steps: 
     *    1 -> Try find Savings in DB
     *    2 -> If not found then return ERROR Response with 404 Not Found Error and Message.
     *    3 -> Else return SUCCESS Response with 200 Ok status with Savings Object
     *    4 -> Catch Server side errors -> If any then return ERRO Response with 500 Internal Server Error with Message.
     * @param id type Long
     * @return ResponseEntity<Object> -> either SUCCESS Response 200 ok | ERROR Response 400 Bad Request | ERROR 500 Internal Server Error
     */
    @GetMapping(value="/get", params={"id"})
    public ResponseEntity<Object> get(@RequestParam(name="id") Long id) {

        try {
            // Find transaction
            Optional<Savings> savings = savingsRepository.findById(id);

            // If savings not found return 404 ERROR
            if(!savings.isPresent()) {
                return Utiles.generateResponse(
                    HttpStatus.NOT_FOUND, 
                    HttpStatus.NOT_FOUND.getReasonPhrase(), 
                    "Savings with id " + id + " not found."
                );
            }

            // Else Return SUCCESS Response 200 OK
            return Utiles.generateResponse(
                HttpStatus.OK, 
                "Success getting Savings with id " + id, 
                savings
            ); 

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,  
                error.getMessage(), 
                "Failed to get Savings from DB."
            );
        }
    }

    /**
     * Used to GET all Savings by User ID and filtered by Name Like query from the DB.
     * Strategy: Validate data coming from FE, Try find All Savings Matching in DB, Catch errors if any.
     * Steps: 
     *    1 -> Try find All Savings Matching in DB
     *    2 -> If not found then return ERROR Response with 404 Not Found Error and Message.
     *    3 -> Else return SUCCESS Response with 200 Ok status with All Savings Matching List
     *    4 -> Catch Server side errors -> If any then return ERRO Response with 500 Internal Server Error with Message.
     * @param userId type Long
     * @param query type String
     * @return ResponseEntity<Object> -> either SUCCESS Response 200 ok | ERROR Response 400 Bad Request | ERROR 500 Internal Server Error
     */
    @GetMapping(value="/get", params={"userId", "query"})
    public ResponseEntity<Object> getFilteredSavings(
        @RequestParam(name="userId") Long userId,
        @RequestParam(name="query") String query
    ) {
        try {
            // Find Savings List
            List<Savings> filteredSavings = savingsRepository.findByUserIdAndNameContaining(userId, query);

            // If Transactions List is empty return 404 ERROR
            if(filteredSavings.size() == 0) {
                return Utiles.generateResponse(
                    HttpStatus.NOT_FOUND, 
                    HttpStatus.NOT_FOUND.getReasonPhrase(), 
                    null
                );
            }

            // Else Return SUCCESS Response 200 OK
            return Utiles.generateResponse(
                HttpStatus.OK, 
                "Success getting Pages.", 
                filteredSavings
            ); 

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,  
                error.getMessage(), 
                null
            );
        }
    }
    
    /**
     * Used to UPDATE Savings by Id in the DB.
     * Strategy: Validate data coming from FE, Try update Savings in DB, Catch Errors if any.
     * Steps: 
     *    1 -> If Request is NULL return ERROR Response with 400 Bad Request Status with message
     *    2 -> Try find Savings by Id in DB
     *    3 -> If not found return ERROR Response 404 Not Found with Message
     *    4 -> Else Update Savings in DB
     *    5 -> If updated in DB then return SUCCESS Response with 200 Ok status with Savings Object
     *    6 -> Catch Server side errors -> If any then return ERRO Response with 500 Internal Server Error with Message.
     * @param id type Long
     * @param requestBody type SavingsRequest
     * @return ResponseEntity<Object> -> either SUCCESS Response 200 ok | ERROR Response 400 Bad Request | ERROR 500 Internal Server Error
     */
    @PutMapping("/{id}/update")
    public ResponseEntity<Object> update(
        @PathVariable Long id, 
        @RequestBody SavingsRequest request
    ) {
        // Check request is not null
        if(request == null) {
            return Utiles.generateResponse(
                HttpStatus.BAD_REQUEST, 
                HttpStatus.BAD_REQUEST.getReasonPhrase(), 
                "Request cannot be NULL."
            );
        }

        try {
            // Find Savings in DB
            Optional<Savings> savings = savingsRepository.findById(id);

            // If not found return 404 ERROR
            if(!savings.isPresent()) {
                return Utiles.generateResponse(
                    HttpStatus.NOT_FOUND, 
                    HttpStatus.NOT_FOUND.getReasonPhrase(), 
                    "Savings with id " + id + " not found."
                );
            }
            
            // Set new data
            savings.get().setAccountId(Long.parseLong(request.getAccountId()));
            savings.get().setName(request.getName());
            savings.get().setTargetAmount(Double.parseDouble(request.getTargetAmount()));

            // Parsing String date from request to SQL Date Type
            Date date = new SimpleDateFormat("yyy-MM-dd").parse(request.getTargetDate());
            savings.get().setTargetDate(new java.sql.Date(date.getTime()));

            // Set new Updated On date
            savings.get().setUpdatedOn(new Timestamp(System.currentTimeMillis()));
            
            // Update Savings
            Savings updatedSavings = savingsRepository.save(savings.get());

            // Return SUCCESS Response 200 OK
            return Utiles.generateResponse(
                HttpStatus.OK, 
                "Success updating Savings.", 
                updatedSavings
            );

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,  
                error.getMessage(), 
                "Failed to update Savings in DB."
            );
        }
        
    }

    /**
     * Used to DELETE an Savings by Id from the DB.
     * Strategy: Validate data coming from FE, Try delete Savings from DB, Catch Errors if any.
     * Steps: 
     *    1 -> If ID is NULL return ERROR Response with 400 Bad Request Status with message
     *    2 -> Try find Savings in DB
     *    3 -> If not found then return ERROR Response 404 Not Found with Message.
     *    4 -> Else delete Savings from DB
     *    5 -> If delete from DB Ok then return SUCCESS Response with 200 Ok status with message
     *    6 -> Catch Server side errors -> If any then return ERRO Response with 500 Internal Server Error with Message.
     * @param id type Long
     * @return ResponseEntity<Object> -> either SUCCESS Response 200 ok | ERROR Response 400 Bad Request | ERROR 500 Internal Server Error
     */
    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Object> delete(@PathVariable Long id) {

        // Validate ID is not null
        if(id == null) {
            return Utiles.generateResponse(
                HttpStatus.BAD_REQUEST, 
                HttpStatus.BAD_REQUEST.getReasonPhrase(), 
                "Savings ID cannot be NULL."
            );
        }

        try {
            // Find Savings
            Optional<Savings> savings = savingsRepository.findById(id);

            // If not found return 404 ERROR
            if(!savings.isPresent()) {
                return Utiles.generateResponse(
                    HttpStatus.NOT_FOUND, 
                    HttpStatus.NOT_FOUND.getReasonPhrase(), 
                    "Savings with id " + id + " not found."
                );
            }
        
            // Delete Savings
            savingsRepository.delete(savings.get());

            // Return SUCCESS Response 200 OK
            return Utiles.generateResponse(
                HttpStatus.OK, 
                "Success deleting the Savings.", 
                null
            );

        } catch (Exception error) {

            // Return ERROR Response 500 Internal Server Error
            return Utiles.generateResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,  
                error.getMessage(), 
                "Failed to delete Savings from DB."
            );
        }
    }
    
}
