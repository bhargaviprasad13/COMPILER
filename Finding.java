package analyzer.report;
public final class Finding {
    private final int lineNumber;
    private final String category;   
    private final Severity severity;
    private final String message;    
    public Finding(int lineNumber, String category, Severity severity, String message) {
        this.lineNumber = lineNumber;
        this.category = category;
        this.severity = severity;
        this.message = message;
    }
    public int getLineNumber() { return lineNumber; }
    public String getCategory() { return category; }
    public Severity getSeverity() { return severity; }
    public String getMessage() { return message; }
    @Override
    public String toString() {
        return String.format("Line %d [%s] %s - %s", lineNumber, severity, category, message);
    }
}
