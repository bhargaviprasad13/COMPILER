package analyzer.report;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Collections;
import java.util.List;
public class ReportCollector {
    private final List<Finding> findings = new ArrayList<>();
    public void add(int line, String category, Severity severity, String message) {
        findings.add(new Finding(line, category, severity, message));
    }
    public void add(Finding f) {
        findings.add(f);
    }
    public List<Finding> getFindings() {
        List<Finding> sorted = new ArrayList<>(findings);
        sorted.sort(Comparator.comparingInt(Finding::getLineNumber)
                              .thenComparing(Finding::getCategory));
        return Collections.unmodifiableList(sorted);
    }
    public boolean isEmpty() {
        return findings.isEmpty();
    }
}
