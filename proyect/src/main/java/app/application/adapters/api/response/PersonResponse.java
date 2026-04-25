package app.application.adapters.api.response;

public record PersonResponse(
    String fullName,
    String document,
    String email,
    String phone,
    String address
) {}
