import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TicketRepositoryTest {

    private final TicketRepository ticketRepository = new TicketRepository();

    @Test
    public void create_ValidInputTest() {
        var ticket = ticketRepository.create("Kolkata", "Bangalore", 2500f, 100, 'A');
        Assertions.assertNotNull(ticket);
    }

    @Test
    public void getById_Test() {
        var ticket = ticketRepository.create("Kolkata", "Bangalore", 2500f, 100, 'A');
        Assertions.assertNotNull(ticket);
        Assertions.assertNotNull(ticketRepository.getById(ticket.id()));
    }

    @Test
    public void getById_InvalidTest() {
        Assertions.assertNull(ticketRepository.getById(200));
    }

    @Test
    public void cancel_ValidTest() {
        var ticket = ticketRepository.create("Kolkata", "Bangalore", 2500f, 100, 'A');
        Assertions.assertNotNull(ticket);

        var cancelledTicket = ticketRepository.cancel(ticket.id());
        Assertions.assertNotNull(cancelledTicket);
        Assertions.assertNull(ticketRepository.getById(cancelledTicket.id()));
    }

    @Test
    public void cancel_InvalidTest() {
        var ticket = ticketRepository.create("Kolkata", "Bangalore", 2500f, 100, 'A');
        Assertions.assertNotNull(ticket);

        var cancelledTicket = ticketRepository.cancel(-1);
        Assertions.assertNull(cancelledTicket);
    }

    @Test
    public void ticketsBySection_ValidTest() {
        for (var i = 0; i < 10; i++) {
            var ticket = ticketRepository.create("Kolkata", "Bangalore", 2500f, i, 'A');
            Assertions.assertNotNull(ticket);
        }
        var tickets = ticketRepository.ticketsBySection('A');
        Assertions.assertEquals(10, tickets.size());
    }

    @Test
    public void ticketsBySection_InvalidTest() {
        for (var i = 0; i < 10; i++) {
            var ticket = ticketRepository.create("Kolkata", "Bangalore", 2500f, i, 'A');
            Assertions.assertNotNull(ticket);
        }
        var tickets = ticketRepository.ticketsBySection('B');
        Assertions.assertEquals(0, tickets.size());
    }
}