# Complaint Tracking System (CLI-Based)

## 1. Project Title

Complaint Tracking System (CLI-Based)

## 2. Quick Start (Clone, Compile, Run)

1. Clone the repository:

```
git clone https://github.com/RishitModi/seLab.git
```

2. Go into the project folder:

```
cd seLab
```

3. Compile the source files:

```
javac src/*.java
```

4. Run the application:

```
java -cp src MainApp
```

## Project Structure

```
.
├── README.md
├── .gitignore
└── src/
    ├── MainApp.java
    ├── ComplaintService.java
    ├── BlackBoxTestRunner.java
    ├── WhiteBoxTestRunner.java
    └── ...
```

## 3. Menu Options

1. Register Complaint (Manual Testing)
2. Run Black Box Tests
3. Run White Box Tests
4. Exit

After running `java -cp src MainApp`:

- Press `1` for manual complaint registration
- Press `2` for black box tests
- Press `3` for white box tests
- Press `4` to exit

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

- Run the program: `java -cp src MainApp`
- Select option `2`
- The system executes all 15 test cases
- PASS/FAIL results are displayed

## 6. How to Run White Box Testing

- Run the program: `java -cp src MainApp`
- Select option `3`
- The system displays:
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
