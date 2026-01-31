package org.team11.tickebook.adminservice.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;


@AllArgsConstructor
@NoArgsConstructor
@Entity
public class AdminProfile {
    @Id
    @GeneratedValue
    private UUID id;
    private UUID userid;
    private String region;
    private Boolean canApproveRoles;
    private Boolean canApproveTheaters;
    private Boolean canApproveChanges;
    private LocalDateTime lastActionAt;
    private String remarks;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getUserid() {
        return userid;
    }

    public void setUserid(UUID userid) {
        this.userid = userid;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Boolean getCanApproveRoles() {
        return canApproveRoles;
    }

    public void setCanApproveRoles(Boolean canApproveRoles) {
        this.canApproveRoles = canApproveRoles;
    }

    public Boolean getCanApproveTheaters() {
        return canApproveTheaters;
    }

    public void setCanApproveTheaters(Boolean canApproveTheaters) {
        this.canApproveTheaters = canApproveTheaters;
    }

    public Boolean getCanApproveChanges() {
        return canApproveChanges;
    }

    public void setCanApproveChanges(Boolean canApproveChanges) {
        this.canApproveChanges = canApproveChanges;
    }

    public LocalDateTime getLastActionAt() {
        return lastActionAt;
    }

    public void setLastActionAt(LocalDateTime lastActionAt) {
        this.lastActionAt = lastActionAt;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
