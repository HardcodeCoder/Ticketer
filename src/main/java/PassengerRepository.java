import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class PassengerRepository {

    private final Map<Long, Passenger> mPassengers = new HashMap<>();

    @Nullable
    public Passenger getById(long id) {
        return mPassengers.get(id);
    }

    @NotNull
    public Passenger create(@NotNull String firstName, @NotNull String lastName, @NotNull String email) {
        var passenger = new Passenger(System.currentTimeMillis(), firstName, lastName, email);
        mPassengers.put(passenger.id(), passenger);
        return passenger;
    }

    public void delete(long id) {
        mPassengers.remove(id);
    }
}