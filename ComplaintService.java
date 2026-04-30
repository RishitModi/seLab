import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplaintService {
    private final boolean systemAvailable;
    private final boolean agentAvailable;
    private final List<Complaint> complaints = new ArrayList<>();
    private final Set<String> validCustomerIds = new HashSet<>();
    private final List<String> agents = new ArrayList<>();
    private int complaintCounter = 1;
    private int agentIndex = 0;

    public ComplaintService(boolean systemAvailable, boolean agentAvailable) {
        this.systemAvailable = systemAvailable;
        this.agentAvailable = agentAvailable;
        seedData();
    }

    public Complaint registerComplaint(String customerId, String details) throws ComplaintException {
        Customer customer = validateCustomer(customerId);
        Complaint complaint = new Complaint(customer, details);
        ensureSystemAvailable();
        recordComplaint(complaint);
        assignSla(complaint);
        assignAgentOrEscalate(complaint);
        sendConfirmation(complaint);
        return complaint;
    }

    private Customer validateCustomer(String customerId) throws InvalidCustomerException {
        Customer customer = new Customer(customerId);
        if (!validCustomerIds.contains(customer.getId())) {
            throw new InvalidCustomerException("Unknown customer ID.");
        }
        return customer;
    }

    private void ensureSystemAvailable() throws SystemUnavailableException {
        if (!systemAvailable) {
            throw new SystemUnavailableException("System is unavailable.");
        }
    }

    private void recordComplaint(Complaint complaint) {
        String id = String.format("CMP-%04d", complaintCounter++);
        complaint.assignId(id);
        complaint.markRecorded();
        complaints.add(complaint);
    }

    private void assignSla(Complaint complaint) {
        complaint.assignSla("STANDARD_48H");
    }

    private void assignAgentOrEscalate(Complaint complaint) {
        try {
            assignAgent(complaint);
        } catch (NoAgentAvailableException e) {
            complaint.escalateToSupervisor();
        }
    }

    private void assignAgent(Complaint complaint) throws NoAgentAvailableException {
        if (!agentAvailable || agents.isEmpty()) {
            throw new NoAgentAvailableException("No agent available.");
        }
        String agent = agents.get(agentIndex % agents.size());
        agentIndex++;
        complaint.assignAgent(agent);
    }

    private void sendConfirmation(Complaint complaint) {
        complaint.markConfirmed();
    }

    private void seedData() {
        validCustomerIds.add("C101");
        validCustomerIds.add("C102");
        validCustomerIds.add("C103");
        validCustomerIds.add("C104");

        agents.add("Agent-A");
        agents.add("Agent-B");
    }
}
