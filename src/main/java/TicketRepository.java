import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TicketRepository {

    private final SeatRepository seatRepository = new SeatRepository();
    private final Map<Integer, Ticket> mTickets = new HashMap<>();
    private int mTicketCounter = 100;

    @Nullable
    public Ticket getById(int id) {
        return mTickets.get(id);
    }

    @Nullable
    public Ticket create(@NotNull String from, @NotNull String to, float price, long passengerId, char section) {
        var seat = seatRepository.bookSeat(mTicketCounter, section);
        if (null == seat) return null;

        var ticket = new Ticket(mTicketCounter++, passengerId, seat, from, to, price);
        mTickets.put(ticket.id(), ticket);
        return ticket;
    }

    @Nullable
    public Ticket modifySeat(@NotNull Ticket ticket, char section) {
        var seat = seatRepository.modifySeat(ticket.id(), section);
        if (null == seat) return null;

        var newTicket = new Ticket(ticket.id(), ticket.passengerId(), seat, ticket.from(), ticket.to(), ticket.price());
        mTickets.put(newTicket.id(), newTicket);
        return newTicket;
    }

    @Nullable
    public Ticket cancel(int ticketId) {
        return mTickets.remove(ticketId);
    }

    @NotNull
    public List<Ticket> ticketsBySection(char section) {
        var result = new ArrayList<Ticket>();
        for (var entry : mTickets.entrySet()) {
            if (entry.getValue().seat().section() == section) result.add(entry.getValue());
        }
        return result;
    }
}