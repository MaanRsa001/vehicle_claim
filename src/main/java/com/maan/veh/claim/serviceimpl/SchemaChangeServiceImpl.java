package com.maan.veh.claim.serviceimpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maan.veh.claim.service.SchemaChangeService;

@Service
public class SchemaChangeServiceImpl implements SchemaChangeService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final Logger logger = LoggerFactory.getLogger(SchemaChangeServiceImpl.class);

    @Override
    public Map<String, Object> compareSchemaChanges() {
        String sql = "CALL compare_schema_changes()";
        Map<String, Object> responseMap = new HashMap<>();
        try {
            List<Map<String, Object>> resultList = jdbcTemplate.queryForList(sql);
            responseMap.put("schemaChanges", resultList);
        } catch (Exception e) {
            logger.error("Error while comparing schema changes: {}", e.getMessage(), e);
            responseMap.put("errorMessage", "Failed to compare schema changes.");
        }
        return responseMap;
    }

    @Override
    public void updateBase() {
        String sql = "CALL update_base()";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            logger.error("Error while updating base schema log: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update base schema log.");
        }
    }

    @Override
    public void updateNew() {
        String sql = "CALL update_new()";
        try {
            jdbcTemplate.execute(sql);
        } catch (Exception e) {
            logger.error("Error while updating new schema log: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to update new schema log.");
        }
    }
}
