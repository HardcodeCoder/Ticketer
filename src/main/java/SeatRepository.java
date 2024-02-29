import org.jetbrains.annotations.Nullable;

public class SeatRepository {

    private final int[] availableSeats = new int[101];
    private int sectionA = 1;
    private int sectionB = 51;

    @Nullable
    public Seat bookSeat(int ticketId, char section) {
        if (!seatAvailableInSection(section)) return null;

        if (section == 'A') {
            availableSeats[sectionA] = ticketId;
            return new Seat(section, sectionA++);
        } else if (section == 'B') {
            availableSeats[sectionB] = ticketId;
            return new Seat(section, sectionB++ - 50);
        }

        return null;
    }

    @Nullable
    public Seat modifySeat(int ticketId, char section) {
        if (section == 'A' && sectionA >= 50) return null;
        if (section == 'B' && sectionB >= 100) return null;
        releaseSeat(ticketId);
        return bookSeat(ticketId, section);
    }

    public void releaseSeat(int ticketId) {
        for (var i = 0; i < availableSeats.length; i++) {
            if (availableSeats[i] == ticketId) {
                availableSeats[i] = 0;
                break;
            }
        }
    }

    private boolean seatAvailableInSection(char section) {
        return section == 'A' && sectionA < 50 ||
               section == 'B' && sectionB < 100;
    }
}