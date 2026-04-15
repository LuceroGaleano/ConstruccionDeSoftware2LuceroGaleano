package application.adapters.api.response;

public record PersonResponse(
    String fullName,
    String identification,
    String email,
    String phone,
    String address
) {}
