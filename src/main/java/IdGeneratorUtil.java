import java.util.UUID;

/**
 * This class provides utility methods for generating unique identifiers (IDs)
 * for books and users.
 */
public class IdGeneratorUtil {
    public static String generateBookId() {
        return "BK" + UUID.randomUUID().toString().substring(0, 8);
    }
    public static String generateUserId() {
        return "USR" + UUID.randomUUID().toString().substring(0, 8);
    }
}
