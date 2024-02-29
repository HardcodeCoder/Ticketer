import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class SeatRepositoryTest {

    private final SeatRepository seatRepository = new SeatRepository();

    @Test
    public void bookSeat_ValidInputTest() {
        var seat = seatRepository.bookSeat(100, 'A');
        Assertions.assertNotNull(seat);
    }

    @Test
    public void bookSeat_ValidInputOvercapacityTest() {
        for (var i = 101; i < 150; i++) {
            var seat = seatRepository.bookSeat(i, 'A');
            Assertions.assertNotNull(seat);
        }

        // Over capacity in section A
        var seat = seatRepository.bookSeat(200, 'A');
        Assertions.assertNull(seat);
    }

    @Test
    public void bookSeat_ValidInValidInputTest() {
        var seat = seatRepository.bookSeat(100, 'C');
        Assertions.assertNull(seat);
    }

    @Test
    public void modifySeat_ValidInputTest() {
        var seat = seatRepository.bookSeat(100, 'A');
        Assertions.assertNotNull(seat);

        var modifiedSeat = seatRepository.modifySeat(100, 'B');
        Assertions.assertNotNull(modifiedSeat);
        Assertions.assertEquals('B', modifiedSeat.section());
    }
}