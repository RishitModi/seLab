public class BlackBoxTestRunner {
    private static final String SEPARATOR = "--------------------------------------------------";

    public static void main(String[] args) {
        BlackBoxTestRunner runner = new BlackBoxTestRunner();
        runner.runAll();
    }

    public void runAll() {
        TestCase[] testCases = buildTestCases();
        int passed = 0;
        for (TestCase testCase : testCases) {
            boolean result = runTestCase(testCase);
            if (result) {
                passed++;
            }
        }
        System.out.println(SEPARATOR);
        System.out.println("Total Test Cases: " + testCases.length);
        System.out.println("Passed: " + passed);
        System.out.println("Failed: " + (testCases.length - passed));
        System.out.println(SEPARATOR);
    }

    private boolean runTestCase(TestCase testCase) {
        System.out.println(SEPARATOR);
        System.out.println("Test Case: " + testCase.id);
        System.out.println("Description: " + testCase.description);
        System.out.println();
        System.out.println("Input:");
        System.out.println("  Customer ID: " + testCase.customerId);
        System.out.println("  Complaint: " + testCase.complaintDetails);
        System.out.println("  System Available: " + testCase.systemAvailable);
        System.out.println("  Agent Available: " + testCase.agentAvailable);

        TestOutcome outcome = execute(testCase);
        boolean pass = matchesExpected(testCase.expectedLines, outcome.actualLines)
                && outcome.additionalChecksPassed;
                printStepByStepComparison(testCase.expectedLines, outcome.actualLines, pass);
        System.out.println(SEPARATOR);
        return pass;
    }

    private TestOutcome execute(TestCase testCase) {
        ComplaintService service = new ComplaintService(testCase.systemAvailable, testCase.agentAvailable);
        ComplaintController controller = new ComplaintController(service);

        if ("TC13".equals(testCase.id)) {
            return runUniqueIdTest(controller, testCase);
        }
        if ("TC14".equals(testCase.id)) {
            return runIndependentProcessingTest(controller, testCase);
        }

        ComplaintResult result = controller.registerComplaintResult(
                testCase.customerId,
                testCase.complaintDetails
        );
        return buildOutcome(result);
    }

    private TestOutcome runUniqueIdTest(ComplaintController controller, TestCase testCase) {
        ComplaintResult first = controller.registerComplaintResult(
                testCase.customerId,
                testCase.complaintDetails
        );
        ComplaintResult second = controller.registerComplaintResult(
                testCase.secondaryCustomerId,
                testCase.secondaryComplaintDetails
        );

        boolean success = first.isSuccess() && second.isSuccess();
        boolean unique = success && !first.getComplaint().getId().equals(second.getComplaint().getId());
        String line = unique ? "Complaint IDs are unique" : "Complaint IDs are not unique";
        return new TestOutcome(new String[] { line }, unique && success);
    }

    private TestOutcome runIndependentProcessingTest(ComplaintController controller, TestCase testCase) {
        ComplaintResult first = controller.registerComplaintResult(
                testCase.customerId,
                testCase.complaintDetails
        );
        ComplaintResult second = controller.registerComplaintResult(
                testCase.secondaryCustomerId,
                testCase.secondaryComplaintDetails
        );

        boolean success = first.isSuccess() && second.isSuccess();
        boolean independent = success
                && first.getComplaint().getStatus() != null
                && second.getComplaint().getStatus() != null;
        String line = independent ? "Complaints processed independently" : "Complaints not processed independently";
        return new TestOutcome(new String[] { line }, independent && success);
    }

    private TestOutcome buildOutcome(ComplaintResult result) {
        if (!result.isSuccess()) {
            String baseMessage = result.getMessage();
            String noRecord = "No complaint record created";
            if (result.getStatus() == ComplaintResult.Status.INVALID_CUSTOMER) {
                return new TestOutcome(new String[] { baseMessage, noRecord }, true);
            }
            return new TestOutcome(new String[] { baseMessage }, true);
        }

        Complaint complaint = result.getComplaint();
        String assignLine = "Assigned to agent";
        if ("ESCALATED".equals(complaint.getStatus())) {
            assignLine = "Escalated to supervisor";
        }
        return new TestOutcome(new String[] {
                "Complaint recorded successfully",
                "SLA assigned",
                assignLine,
                "Confirmation sent"
        }, true);
    }

    private boolean matchesExpected(String[] expected, String[] actual) {
        int index = 0;
        for (String expectedLine : expected) {
            boolean found = false;
            for (int i = index; i < actual.length; i++) {
                if (actual[i].contains(expectedLine)) {
                    found = true;
                    index = i + 1;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

        private void printStepByStepComparison(String[] expected, String[] actual, boolean pass) {
                System.out.println();
                System.out.println("Expected:");
                for (String expectedLine : expected) {
                        System.out.println("  " + expectedLine);
                }

                System.out.println();
                System.out.println("Actual:");
                if (actual.length == 0) {
                        System.out.println("  (none)");
                } else {
                        for (String actualLine : actual) {
                                System.out.println("  " + actualLine);
                        }
                }

                System.out.println();
                System.out.println("Match: " + (pass ? "PASS" : "FAIL"));
        }

    private TestCase[] buildTestCases() {
        return new TestCase[] {
                new TestCase("TC1",
                        "Verify successful complaint registration",
                        "C101",
                        "Internet not working",
                        true,
                        true,
                        new String[] {
                                "Complaint recorded successfully",
                                "SLA assigned",
                                "Assigned to agent",
                                "Confirmation sent"
                        }),
                new TestCase("TC2",
                        "Reject invalid customer",
                        "C999",
                        "Speed issue",
                        true,
                        true,
                        new String[] {
                                "Customer validation failed"
                        }),
                new TestCase("TC3",
                        "No complaint record for invalid customer",
                        "C999",
                        "Billing issue",
                        true,
                        true,
                        new String[] {
                                "Customer validation failed",
                                "No complaint record created"
                        }),
                new TestCase("TC4",
                        "Store complaint successfully",
                        "C102",
                        "Router reboot required",
                        true,
                        true,
                        new String[] {
                                "Complaint recorded successfully"
                        }),
                new TestCase("TC5",
                        "Assign SLA for valid complaint",
                        "C103",
                        "Frequent disconnections",
                        true,
                        true,
                        new String[] {
                                "SLA assigned"
                        }),
                new TestCase("TC6",
                        "Assign agent when available",
                        "C101",
                        "No signal",
                        true,
                        true,
                        new String[] {
                                "Assigned to agent"
                        }),
                new TestCase("TC7",
                        "Send confirmation for valid flow",
                        "C102",
                        "Slow speed",
                        true,
                        true,
                        new String[] {
                                "Confirmation sent"
                        }),
                new TestCase("TC8",
                        "Reject missing complaint details",
                        "C101",
                        " ",
                        true,
                        true,
                        new String[] {
                                "Complaint validation failed"
                        }),
                new TestCase("TC9",
                        "Show retry message when system is unavailable",
                        "C101",
                        "Line down",
                        false,
                        true,
                        new String[] {
                                "System unavailable"
                        }),
                new TestCase("TC10",
                        "Escalate when no agent is available",
                        "C102",
                        "Installation pending",
                        true,
                        false,
                        new String[] {
                                "Escalated to supervisor"
                        }),
                new TestCase("TC11",
                        "SLA assigned before agent allocation",
                        "C103",
                        "Packet loss",
                        true,
                        true,
                        new String[] {
                                "Complaint recorded successfully",
                                "SLA assigned",
                                "Assigned to agent"
                        }),
                new TestCase("TC12",
                        "Stop flow after validation failure",
                        "C999",
                        "Device issue",
                        true,
                        true,
                        new String[] {
                                "Customer validation failed",
                                "No complaint record created"
                        }),
                new TestCase("TC13",
                        "Assign unique identifiers",
                        "C101",
                        "Service outage",
                        true,
                        true,
                        new String[] {
                                "Complaint IDs are unique"
                        },
                        "C102",
                        "Account issue"),
                new TestCase("TC14",
                        "Process complaints independently",
                        "C101",
                        "Payment update",
                        true,
                        true,
                        new String[] {
                                "Complaints processed independently"
                        },
                        "C103",
                        "Signal drops"),
                new TestCase("TC15",
                        "Complete flow executed successfully",
                        "C102",
                        "New connection request",
                        true,
                        true,
                        new String[] {
                                "Complaint recorded successfully",
                                "SLA assigned",
                                "Assigned to agent",
                                "Confirmation sent"
                        })
        };
    }

    private static class TestOutcome {
        private final String[] actualLines;
        private final boolean additionalChecksPassed;

        private TestOutcome(String[] actualLines, boolean additionalChecksPassed) {
            this.actualLines = actualLines;
            this.additionalChecksPassed = additionalChecksPassed;
        }
    }

    private static class TestCase {
        private final String id;
        private final String description;
        private final String customerId;
        private final String complaintDetails;
        private final boolean systemAvailable;
        private final boolean agentAvailable;
        private final String[] expectedLines;
        private final String secondaryCustomerId;
        private final String secondaryComplaintDetails;

        private TestCase(String id,
                         String description,
                         String customerId,
                         String complaintDetails,
                         boolean systemAvailable,
                         boolean agentAvailable,
                         String[] expectedLines) {
            this(id, description, customerId, complaintDetails, systemAvailable, agentAvailable, expectedLines, null, null);
        }

        private TestCase(String id,
                         String description,
                         String customerId,
                         String complaintDetails,
                         boolean systemAvailable,
                         boolean agentAvailable,
                         String[] expectedLines,
                         String secondaryCustomerId,
                         String secondaryComplaintDetails) {
            this.id = id;
            this.description = description;
            this.customerId = customerId;
            this.complaintDetails = complaintDetails;
            this.systemAvailable = systemAvailable;
            this.agentAvailable = agentAvailable;
            this.expectedLines = expectedLines;
            this.secondaryCustomerId = secondaryCustomerId;
            this.secondaryComplaintDetails = secondaryComplaintDetails;
        }
    }
}
