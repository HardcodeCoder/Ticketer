import org.jetbrains.annotations.NotNull;

public record Passenger(long id, @NotNull String firstname, @NotNull String lastName, @NotNull String email) {}