package com.elphie.savings.libs;

// =============================================================================
// File Name: libs/Utiles.java
// File Description:
// This file contains handy methods to help increase development.
// =============================================================================

// =============================================================================
// Imports
// =============================================================================
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.elphie.savings.models.Savings;

// =============================================================================
// Class
// =============================================================================
public class Utiles {

    /**
     * Used to generate custome JSON Http Responses
     * @param status type HttpStatus -> The Response status 200 OK | 400 Client Error | 404 Not Found | 500 Server Error
     * @param message type String -> The Response message
     * @param data type Object | null -> If Success add Data Object else set as null
     * @return 
     */
    public static ResponseEntity<Object> generateResponse(HttpStatus status, String message, Object data) {

        // Create the Response Object as a Map
        Map<String, Object> responseMap = new HashMap<String, Object>();

        // Populate Response
        responseMap.put("timestamp", new Date());
        responseMap.put("status", status.value());
        responseMap.put("ok", status.value() == 200 ? true : false);
        responseMap.put("message", message);
        if(data != null) responseMap.put("data", data);

        // Return Response Object
        return new ResponseEntity<Object>(responseMap, status);
    }

    /**
     * Used to validate Savings Object Data as per DB Policies.
     *    1 -> NOT NULL Data Policy
     *    2 -> NOT VALID Data Policy
     * @param savings type Savings
     * @return errors type ArrayList<String>
     */
    public static ArrayList<String> validateSavings(Savings savings) {

        // Local variable errors
        ArrayList<String> errors = new ArrayList<>();

        // TODO Validate NOT NULL Policy violations
        

        // TODO Validate DATA TYPE Policy Violations
        

        // Return isValid value
        return errors;
    }

}
