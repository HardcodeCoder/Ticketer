import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class PassengerRepositoryTest {

    private final PassengerRepository passengerRepository = new PassengerRepository();

    @Test
    public void getById_WithValidIdTest() {
        var passenger = passengerRepository.create("Test", "Name", "test@email.com");
        Assertions.assertNotNull(passenger);
        Assertions.assertNotNull(passengerRepository.getById(passenger.id()));
    }

    @Test
    public void getById_WithInValidIdTest() {
        var passenger = passengerRepository.create("Test", "Name", "test@email.com");
        Assertions.assertNotNull(passenger);
        Assertions.assertNull(passengerRepository.getById(passenger.id() + 100));
    }

    @Test
    public void delete_Test() {
        var passenger = passengerRepository.create("Test", "Name", "test@email.com");
        Assertions.assertNotNull(passenger);
        passengerRepository.delete(passenger.id());

        Assertions.assertNull(passengerRepository.getById(passenger.id()));
    }
}