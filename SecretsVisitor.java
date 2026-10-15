package analyzer.rules;
import analyzer.report.ReportCollector;
import analyzer.report.Severity;
import analyzer.parser.JavaParser;
import analyzer.parser.JavaParserBaseVisitor;
public class SecretsVisitor extends JavaParserBaseVisitor<Void> {
    private static final String CATEGORY = "HARDCODED_SECRET";
    private final ReportCollector collector;

    public SecretsVisitor(ReportCollector collector) {
        this.collector = collector;
    }
    @Override
    public Void visitVariableDeclarator(JavaParser.VariableDeclaratorContext ctx) {
        JavaParser.VariableInitializerContext init = ctx.variableInitializer();
        if (init != null && init.expression() != null) {
            String name = ctx.variableDeclaratorId().getText();
            check(name, init.expression(), ctx.getStart().getLine());
        }
        return visitChildren(ctx);
    }
    @Override
    public Void visitExpression(JavaParser.ExpressionContext ctx) {
        if (ctx.bop != null && "=".equals(ctx.bop.getText()) && ctx.expression().size() == 2) {
            String lhs = ctx.expression(0).getText();
            String name = lhs.substring(lhs.lastIndexOf('.') + 1); // this.x -> x
            check(name, ctx.expression(1), ctx.getStart().getLine());
        }
        return visitChildren(ctx);
    }
    private void check(String name, JavaParser.ExpressionContext value, int line) {
        String literal = stringLiteralValue(value);
        if (literal == null || literal.isEmpty()) return;      // not a literal, or empty
        if (!SecretNamePatterns.looksLikeSecretName(name)) return;

        collector.add(line, CATEGORY, Severity.HIGH,
                "Hardcoded string assigned to '" + name + "'; load it from env/config instead");
    }
    private String stringLiteralValue(JavaParser.ExpressionContext e) {
        if (e.primary() == null || e.primary().literal() == null) return null;
        var lit = e.primary().literal().STRING_LITERAL();
        if (lit == null) return null;
        String text = lit.getText();
        return text.substring(1, text.length() - 1);
    }
}
