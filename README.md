# Complaint Tracking System (CLI-Based)

## 1. Project Title

Complaint Tracking System (CLI-Based)

## 2. How to Compile and Run

Commands:

```
javac *.java
java MainApp
```

Run MainApp directly:

```
java MainApp
```

## 3. Menu Options

1. Register Complaint (Manual Testing)
2. Run Black Box Tests
3. Run White Box Tests
4. Exit

## 4. Dummy Data for Manual Testing

Valid Customer IDs:
- C101
- C102
- C103
- C104

Example Inputs:

Valid Case:
Customer ID: C101
Complaint: Internet not working

Invalid Case:
Customer ID: INVALID
Complaint: Slow internet

Edge Case:
Customer ID: C102
Complaint: (leave empty)

## 5. How to Run Black Box Testing

- Select option 2
- System will automatically execute all 15 test cases
- PASS/FAIL results will be displayed

## 6. How to Run White Box Testing

- Select option 3
- System will display:
	- Control Flow Graph (CFG)
	- Cyclomatic Complexity
	- Independent Paths
	- Execution of 15 test cases
	- Coverage Summary

## 7. Features Implemented

- Complaint Registration
- Customer Validation
- SLA Assignment
- Agent Assignment / Supervisor Escalation
- Black Box Testing (15 test cases)
- White Box Testing (15 test cases)
- CFG + Cyclomatic Complexity
- CLI-based execution

## 8. Notes

- No database used (in-memory simulation)
- No frontend (CLI-based)
- Deterministic outputs for testing
