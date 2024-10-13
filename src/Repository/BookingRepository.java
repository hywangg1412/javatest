package Repository;

import Model.Booking;
import View.AppTools;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class BookingRepository implements IBookingRepository{

    @Override
    public Set<Booking> readFile() {
        Set<Booking> bookingSet = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(path + bookingPath))){
            String line;
            while ((line = reader.readLine()) != null){
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
                bookingSet.add(booking);
            }
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
        return bookingSet;
    }

    @Override
    public void writeFile(Set<Booking> entities) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path + bookingPath))){
            for (Booking booking :entities){
                writer.write(String.join(",",
                        booking.getBookingID(),
                        AppTools.localDateToString(booking.getBookingDate()),
                        AppTools.localDateToString(booking.getStartDate()),
                        AppTools.localDateToString(booking.getEndDate()),
                        booking.getCustomerID(),
                        booking.getServiceID()));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
