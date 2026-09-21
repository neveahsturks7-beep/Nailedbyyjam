import java.io.*;

public class BookingManager {

private static final String FILE_NAME =
"bookings.txt";


public static void saveBooking(
Booking booking
) {

try {

FileWriter writer =
new FileWriter(
FILE_NAME,
true
);


writer.write(
"================================\n"
);

writer.write(
"NEW BOOKING\n"
);

writer.write(
booking.toString()
);

writer.write(
"\n================================\n\n"
);


writer.close();


System.out.println(
"Booking saved!"
);


} catch (IOException e) {

System.out.println(
"Could not save booking: " +
e.getMessage()
);

}

}

}