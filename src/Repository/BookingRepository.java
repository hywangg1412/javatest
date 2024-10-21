package Repository;

import Model.Booking;
import View.AppTools;

import java.io.*;
import java.util.TreeSet;

public class BookingRepository implements IBookingRepository{

    @Override
    public TreeSet<Booking> readFile() {
        TreeSet<Booking> bookingList = new TreeSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path + bookingPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length < 6) {
                    System.out.println("-> Skipping " + line);
                    continue;
                }
                Booking booking = new Booking(
                        data[0],
                        data[1],
                        data[2],
                        data[3],
                        data[4],
                        data[5]
                );
                bookingList.add(booking);
            }
        } catch (IOException e) {
            System.out.println("-> Error reading file: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return bookingList;
    }


    @Override
    public void writeFile(TreeSet<Booking> entities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + bookingPath))) {
            for (Booking booking : entities) {
                String line = booking.getBookingID() + ","
                        + AppTools.localDateToString(booking.getBookingDate()) + ","
                        + AppTools.localDateToString(booking.getStartDate()) + ","
                        + AppTools.localDateToString(booking.getEndDate()) + ","
                        + booking.getCustomerID() + ","
                        + booking.getServiceID();

                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
