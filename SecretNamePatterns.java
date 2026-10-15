package analyzer.rules;
import java.util.Set;
public final class SecretNamePatterns {
    private static final Set<String> SECRET_WORDS = Set.of(
            "password", "passwd", "pwd", "pass", "passphrase",
            "secret", "token", "key", "apikey", "credential", "credentials"
    );
    private SecretNamePatterns() {}
    public static boolean looksLikeSecretName(String name) {
        if (name == null || name.isEmpty()) return false;
        String spaced = name.replaceAll("([a-z0-9])([A-Z])", "$1_$2").toLowerCase();
        for (String word : spaced.split("[^a-z0-9]+")) {
            if (SECRET_WORDS.contains(word)) return true;
        }
        return false;
    }
}
