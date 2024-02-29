import org.jetbrains.annotations.NotNull;

public record Ticket(int id,
                     long passengerId,
                     @NotNull Seat seat,
                     @NotNull String from,
                     @NotNull String to,
                     float price) {

}