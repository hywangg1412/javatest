package Repository;

import Model.Booking;

import java.util.Set;
import java.util.TreeSet;

public interface IBookingRepository extends Repository<Booking , TreeSet<Booking>> {

    final String bookingPath = "\\Data\\booking.csv";

    public TreeSet<Booking> readFile();

    public void writeFile(TreeSet<Booking> entities);
}
