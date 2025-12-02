package [packageName].infrastructure.adapters.input.dto.security;

public class AuthDTO {
    public record LoginRequest(String username, String password) {
    }

    public record Response(String message, String token) {
    }
}
