public class ComplaintResult {
    public enum Status {
        SUCCESS,
        INVALID_CUSTOMER,
        INVALID_COMPLAINT,
        SYSTEM_UNAVAILABLE,
        ERROR
    }

    private final Status status;
    private final Complaint complaint;
    private final String message;

    private ComplaintResult(Status status, Complaint complaint, String message) {
        this.status = status;
        this.complaint = complaint;
        this.message = message;
    }

    public static ComplaintResult success(Complaint complaint) {
        return new ComplaintResult(Status.SUCCESS, complaint, null);
    }

    public static ComplaintResult failure(Status status, String message) {
        return new ComplaintResult(status, null, message);
    }

    public boolean isSuccess() {
        return status == Status.SUCCESS;
    }

    public Status getStatus() {
        return status;
    }

    public Complaint getComplaint() {
        return complaint;
    }

    public String getMessage() {
        return message;
    }
}
