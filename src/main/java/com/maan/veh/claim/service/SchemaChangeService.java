package com.maan.veh.claim.service;
import java.util.Map;

public interface SchemaChangeService {
    Map<String, Object> compareSchemaChanges();
    void updateBase();
    void updateNew();
}

