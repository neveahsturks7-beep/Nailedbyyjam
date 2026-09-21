import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class Server {

public static void main(String[] args) {

try {

ServerSocket serverSocket =
new ServerSocket(8080);


System.out.println(
"================================"
);

System.out.println(
"@NAILEDBYYJAM SERVER IS RUNNING!"
);

System.out.println(
"Website:"
);

System.out.println(
"http://localhost:8080"
);

System.out.println(
"Admin:"
);

System.out.println(
"http://localhost:8080/admin.html"
);

System.out.println(
"================================"
);


while (true) {

Socket client =
serverSocket.accept();

handleRequest(client);

}


} catch (IOException e) {

System.out.println(
"Server error: " +
e.getMessage()
);

}

}


public static void handleRequest(
Socket client
) {

try {

BufferedReader input =
new BufferedReader(
new InputStreamReader(
client.getInputStream(),
StandardCharsets.UTF_8
)
);


OutputStream output =
client.getOutputStream();


String request =
input.readLine();


if (request == null) {

client.close();

return;

}


System.out.println(
"Request: " +
request
);


int contentLength = 0;

String line;


while (
(line = input.readLine()) != null
&& !line.isEmpty()
) {

if (
line.toLowerCase()
.startsWith(
"content-length:"
)
) {

contentLength =
Integer.parseInt(
line.substring(
line.indexOf(":") + 1
).trim()
);

}

}


String[] parts =
request.split(" ");


if (parts.length < 2) {

client.close();

return;

}


String path =
parts[1];


// =============================
// BOOKING
// =============================

if (
path.equals("/booking")
&&
request.startsWith("POST")
) {

char[] buffer =
new char[contentLength];


int read = 0;


while (
read < contentLength
) {

int amount =
input.read(
buffer,
read,
contentLength - read
);


if (amount == -1) {

break;

}


read += amount;

}


String bookingData =
new String(
buffer,
0,
read
);


saveBooking(
bookingData
);


sendMessage(
output,
"<h1>Booking Received! 💗</h1>" +
"<p>Thank you for booking with @Nailedbyyjam.</p>"
);

}


// =============================
// ADMIN BOOKINGS
// =============================

else if (
path.equals(
"/admin/bookings"
)
) {

sendBookings(
output
);

}


// =============================
// WEBSITE FILES
// =============================

else {

serveFile(
output,
path
);

}


client.close();


} catch (Exception e) {

System.out.println(
"Request error: " +
e.getMessage()
);

}

}


// =============================
// SAVE BOOKING
// =============================

public static void saveBooking(
String bookingData
) {

try {

FileWriter writer =
new FileWriter(
"bookings.txt",
true
);


writer.write(
"================================\n"
);

writer.write(
"NEW BOOKING REQUEST\n"
);

writer.write(
bookingData
);

writer.write(
"\n================================\n\n"
);


writer.close();


System.out.println(
"BOOKING SAVED!"
);


} catch (IOException e) {

System.out.println(
"Could not save booking: " +
e.getMessage()
);

}

}


// =============================
// ADMIN BOOKING PAGE
// =============================

public static void sendBookings(
OutputStream output
) {

try {

File file =
new File(
"bookings.txt"
);


String bookings = "";


if (file.exists()) {

BufferedReader reader =
new BufferedReader(
new FileReader(file)
);


String line;


while (
(line = reader.readLine())
!= null
) {

bookings +=
line + "\n";

}


reader.close();

}

else {

bookings =
"No bookings yet.";

}


String safeBookings =
bookings
.replace(
"&",
"&amp;"
)
.replace(
"<",
"&lt;"
)
.replace(
">",
"&gt;"
);


String response =

"<!DOCTYPE html>" +

"<html>" +

"<head>" +

"<title>" +
"@Nailedbyyjam Admin" +
"</title>" +

"</head>" +

"<body style='" +

"background:black;" +
"color:white;" +
"font-family:Arial;" +
"padding:40px;" +

"'>" +

"<h1 style='color:#ff1493;'>" +

"@NAILEDBYYJAM BOOKINGS" +

"</h1>" +

"<pre style='" +

"background:#151515;" +
"padding:20px;" +
"border-radius:10px;" +

"'>" +

safeBookings +

"</pre>" +

"</body>" +

"</html>";


byte[] data =
response.getBytes(
StandardCharsets.UTF_8
);


String header =

"HTTP/1.1 200 OK\r\n" +

"Content-Type: " +
"text/html; charset=UTF-8\r\n" +

"Content-Length: " +
data.length +

"\r\n\r\n";


output.write(
header.getBytes(
StandardCharsets.UTF_8
)
);


output.write(data);

output.flush();


} catch (IOException e) {

System.out.println(
"Could not load bookings."
);

}

}


// =============================
// SERVE FILES
// =============================

public static void serveFile(
OutputStream output,
String path
) {

try {

if (
path.equals("/")
||
path.equals("")
) {

path =
"/index.html";

}


if (path.contains("?")) {

path =
path.substring(
0,
path.indexOf("?")
);

}


File file =
new File(
".." + path
);


if (!file.exists()) {

sendMessage(
output,
"<h1>404 - File Not Found</h1>"
);

return;

}


FileInputStream input =
new FileInputStream(file);


byte[] data =
input.readAllBytes();


input.close();


String contentType =
getContentType(path);


String header =

"HTTP/1.1 200 OK\r\n" +

"Content-Type: " +
contentType +
"\r\n" +

"Content-Length: " +
data.length +
"\r\n" +

"\r\n";


output.write(
header.getBytes(
StandardCharsets.UTF_8
)
);


output.write(data);

output.flush();


} catch (IOException e) {

System.out.println(
"Could not load file: " +
e.getMessage()
);

}

}


// =============================
// FILE TYPES
// =============================

public static String getContentType(
String path
) {

if (path.endsWith(".html")) {

return
"text/html; charset=UTF-8";

}


if (path.endsWith(".css")) {

return
"text/css";

}


if (path.endsWith(".js")) {

return
"application/javascript";

}


if (path.endsWith(".png")) {

return
"image/png";

}


if (
path.endsWith(".jpg")
||
path.endsWith(".jpeg")
) {

return
"image/jpeg";

}


if (path.endsWith(".gif")) {

return
"image/gif";

}


return
"application/octet-stream";

}


// =============================
// SIMPLE RESPONSE
// =============================

public static void sendMessage(
OutputStream output,
String message
) {

try {

String response =

"<!DOCTYPE html>" +

"<html>" +

"<head>" +

"<title>" +
"@Nailedbyyjam" +
"</title>" +

"</head>" +

"<body style='" +

"background:black;" +
"color:white;" +
"font-family:Arial;" +
"text-align:center;" +
"padding:100px;" +

"'>" +

"<div style='color:#ff1493;'>" +

message +

"</div>" +

"</body>" +

"</html>";


byte[] data =
response.getBytes(
StandardCharsets.UTF_8
);


String header =

"HTTP/1.1 200 OK\r\n" +

"Content-Type: " +
"text/html; charset=UTF-8\r\n" +

"Content-Length: " +
data.length +

"\r\n\r\n";


output.write(
header.getBytes(
StandardCharsets.UTF_8
)
);


output.write(data);

output.flush();


} catch (IOException e) {

System.out.println(
"Could not send response."
);

}

}

}