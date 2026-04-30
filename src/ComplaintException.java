public class ComplaintException extends Exception {
    public ComplaintException(String message) {
        super(message);
    }
}

class InvalidCustomerException extends ComplaintException {
    public InvalidCustomerException(String message) {
        super(message);
    }
}

class SystemUnavailableException extends ComplaintException {
    public SystemUnavailableException(String message) {
        super(message);
    }
}

class NoAgentAvailableException extends ComplaintException {
    public NoAgentAvailableException(String message) {
        super(message);
    }
}

class InvalidComplaintException extends ComplaintException {
    public InvalidComplaintException(String message) {
        super(message);
    }
}
