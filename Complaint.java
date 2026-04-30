public class Complaint {
    private String id;
    private final Customer customer;
    private final String details;
    private String sla;
    private String assignedTo;
    private String status;

    public Complaint(Customer customer, String details) throws InvalidComplaintException {
        if (customer == null) {
            throw new InvalidComplaintException("Customer is required.");
        }
        if (details == null || details.trim().isEmpty()) {
            throw new InvalidComplaintException("Complaint details are required.");
        }
        this.customer = customer;
        this.details = details.trim();
        this.status = "NEW";
    }

    public void assignId(String id) {
        this.id = id;
    }

    public void markRecorded() {
        this.status = "RECORDED";
    }

    public void assignSla(String sla) {
        this.sla = sla;
    }

    public void assignAgent(String agent) {
        this.assignedTo = agent;
        this.status = "ASSIGNED";
    }

    public void escalateToSupervisor() {
        this.assignedTo = "SUPERVISOR";
        this.status = "ESCALATED";
    }

    public void markConfirmed() {
        if ("ESCALATED".equals(this.status)) {
            return;
        }
        this.status = "CONFIRMED";
    }

    public String getId() {
        return id;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getDetails() {
        return details;
    }

    public String getSla() {
        return sla;
    }

    public String getAssignedTo() {
        return assignedTo;
    }

    public String getStatus() {
        return status;
    }
}
