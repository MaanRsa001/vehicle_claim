package com.maan.veh.claim.controller;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.SchemaChangeService;

@RestController
@RequestMapping("/api/schema")
public class SchemaChangeController {

    @Autowired
    private SchemaChangeService schemaChangeService;

    // API to call compare_schema_changes stored procedure
    @PatchMapping("/compare")
    public ResponseEntity<CommonResponse> compareSchemaChanges() {
        Map<String, Object> comparisonResult = schemaChangeService.compareSchemaChanges();
        
        CommonResponse response = new CommonResponse();
        response.setMessage("Schema comparison completed");
        response.setResponse(comparisonResult);
        response.setErrors(null);
        response.setIsError(false);

        return ResponseEntity.ok(response);
    }

    // API to call update_base stored procedure
    @PatchMapping("/update-base")
    public ResponseEntity<CommonResponse> updateBase() {
        schemaChangeService.updateBase();
        
        CommonResponse response = new CommonResponse();
        response.setMessage("Base schema log updated successfully");
        response.setResponse(null);
        response.setErrors(null);
        response.setIsError(false);

        return ResponseEntity.ok(response);
    }

    // API to call update_new stored procedure
    @PatchMapping("/update-new")
    public ResponseEntity<CommonResponse> updateNew() {
        schemaChangeService.updateNew();
        
        CommonResponse response = new CommonResponse();
        response.setMessage("New schema log updated successfully");
        response.setResponse(null);
        response.setErrors(null);
        response.setIsError(false);

        return ResponseEntity.ok(response);
    }
}

