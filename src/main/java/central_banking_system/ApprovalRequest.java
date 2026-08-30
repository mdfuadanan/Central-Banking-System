package central_banking_system;

import java.time.LocalDateTime;

public class ApprovalRequest {
    private String requestId;
    private String administratorId;
    private String administratorName;
    private ApprovalType requestType;
    private String description;
    private LocalDateTime submittedAt;
    private ApprovalStatus status;
    private String governorRemarks;
    private String governorId;
    private LocalDateTime approvalTime;
    private String targetId;
    private String payload;

    public ApprovalRequest(String requestId, String administratorId, String administratorName,
                           ApprovalType requestType, String description, LocalDateTime submittedAt,
                           ApprovalStatus status, String governorRemarks, String governorId,
                           LocalDateTime approvalTime, String targetId, String payload) {
        this.requestId = requestId;
        this.administratorId = administratorId;
        this.administratorName = administratorName;
        this.requestType = requestType;
        this.description = description;
        this.submittedAt = submittedAt;
        this.status = status;
        this.governorRemarks = governorRemarks;
        this.governorId = governorId;
        this.approvalTime = approvalTime;
        this.targetId = targetId;
        this.payload = payload;
    }

    public String getRequestId() {
        return requestId;
    }

    public String getAdministratorId() {
        return administratorId;
    }

    public String getAdministratorName() {
        return administratorName;
    }

    public ApprovalType getRequestType() {
        return requestType;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public ApprovalStatus getStatus() {
        return status;
    }

    public void setStatus(ApprovalStatus status) {
        this.status = status;
    }

    public String getGovernorRemarks() {
        return governorRemarks;
    }

    public void setGovernorRemarks(String governorRemarks) {
        this.governorRemarks = governorRemarks;
    }

    public String getGovernorId() {
        return governorId;
    }

    public void setGovernorId(String governorId) {
        this.governorId = governorId;
    }

    public LocalDateTime getApprovalTime() {
        return approvalTime;
    }

    public void setApprovalTime(LocalDateTime approvalTime) {
        this.approvalTime = approvalTime;
    }

    public String getTargetId() {
        return targetId;
    }

    public String getPayload() {
        return payload;
    }
}
