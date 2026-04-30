public class ComplaintController {
    private final ComplaintService service;

    public ComplaintController(ComplaintService service) {
        this.service = service;
    }

    public String registerComplaint(String customerId, String details) {
        ComplaintResult result = registerComplaintResult(customerId, details);
        if (result.isSuccess()) {
            return buildSuccessMessage(result.getComplaint());
        }
        return result.getMessage();
    }

    public ComplaintResult registerComplaintResult(String customerId, String details) {
        try {
            Complaint complaint = service.registerComplaint(customerId, details);
            return ComplaintResult.success(complaint);
        } catch (InvalidCustomerException e) {
            return ComplaintResult.failure(ComplaintResult.Status.INVALID_CUSTOMER,
                    "Customer validation failed: " + e.getMessage());
        } catch (InvalidComplaintException e) {
            return ComplaintResult.failure(ComplaintResult.Status.INVALID_COMPLAINT,
                    "Complaint validation failed: " + e.getMessage());
        } catch (SystemUnavailableException e) {
            return ComplaintResult.failure(ComplaintResult.Status.SYSTEM_UNAVAILABLE,
                    "System unavailable. Please try again later.");
        } catch (ComplaintException e) {
            return ComplaintResult.failure(ComplaintResult.Status.ERROR,
                    "Unable to register complaint: " + e.getMessage());
        }
    }

    private String buildSuccessMessage(Complaint complaint) {
        StringBuilder message = new StringBuilder();
        message.append("Complaint recorded successfully.");
        message.append(" ID: ").append(complaint.getId());
        message.append(" SLA: ").append(complaint.getSla());
        message.append(" Assigned to: ").append(complaint.getAssignedTo()).append(".");
        if ("ESCALATED".equals(complaint.getStatus())) {
            message.append(" Complaint escalated to supervisor.");
        }
        message.append(" Confirmation sent.");
        return message.toString();
    }
}
