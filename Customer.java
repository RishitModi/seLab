public class Customer {
    private final String id;

    public Customer(String id) throws InvalidCustomerException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidCustomerException("Customer ID is required.");
        }
        this.id = id.trim();
    }

    public String getId() {
        return id;
    }
}
