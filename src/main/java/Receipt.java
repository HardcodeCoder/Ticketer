import org.jetbrains.annotations.NotNull;

public record Receipt(@NotNull Ticket ticket, @NotNull Passenger passenger) {}