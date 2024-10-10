package com.maan.veh.claim.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vc_claim_status")
@IdClass(VcClaimStatusId.class)
public class VcClaimStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Id
    @Column(name = "claim_no", length = 50, nullable = false)
    private String claimNo;

    @Column(name = "garage_id", length = 50)
    private String garageId;

    @Column(name = "dealer_id", length = 50)
    private String dealerId;

    @Column(name = "surveyor_id", length = 50)
    private String surveyorId;

    @Column(name = "current_status", length = 50)
    private String currentStatus;

    @Column(name = "next_status", length = 50)
    private String nextStatus;

    @Column(name = "updated_at")
    private java.util.Date updatedAt;

    @Column(name = "created_at")
    private java.util.Date createdAt;
}
