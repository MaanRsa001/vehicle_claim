package com.maan.veh.claim.entity;

import java.time.LocalDateTime;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "eagle_api_transaction_log")
public class ApiTransactionLog {
	
	@Id
	@Column(name = "sno")
	private Long sno;  // Primary Key

	
    @Column(name = "request_time")
    private LocalDateTime requestTime;

    @Column(name = "response_time")
    private LocalDateTime responseTime;

    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "endpoint", length = 255)
    private String endpoint;

    @Column(name = "status", length = 255)  // Renamed to match STATUSV column
    private String status;

    @Lob
    @Column(name = "request")
    private String request;

    @Lob
    @Column(name = "response")
    private String response;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @Lob
    @Column(name = "additional_info")
    private String additionalInfo;
}
