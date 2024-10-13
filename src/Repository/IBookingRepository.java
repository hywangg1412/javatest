package Repository;

import Model.Booking;

import java.util.HashSet;
import java.util.Set;

public interface IBookingRepository extends Repository<Booking , Set<Booking>> {

    final String bookingPath = "\\Data\\booking.csv";

    public Set<Booking> readFile();

    public void writeFile(Set<Booking> entities);
}
