public class Booking {

private String name;
private String email;
private String phone;
private String date;
private String time;
private String nailLength;
private String designs;
private String instagram;
private String inspiration;
private double total;


public Booking(
String name,
String email,
String phone,
String date,
String time,
String nailLength,
String designs,
String instagram,
String inspiration,
double total
) {

this.name = name;
this.email = email;
this.phone = phone;
this.date = date;
this.time = time;
this.nailLength = nailLength;
this.designs = designs;
this.instagram = instagram;
this.inspiration = inspiration;
this.total = total;

}


public String toString() {

return
"Name: " + name +
"\nEmail: " + email +
"\nPhone: " + phone +
"\nDate: " + date +
"\nTime: " + time +
"\nNail Length: " + nailLength +
"\nDesigns: " + designs +
"\nInstagram: " + instagram +
"\nInspiration: " + inspiration +
"\nTotal: $" + total;

}

}