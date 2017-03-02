Hotel Booking System using Spring boot and hsqldb
------------------------
:spring_version: current
:toc:
:project_id: hotel-management-system-test
:spring_version: current
:spring_boot_version: 1.5.1.RELEASE

This guide walk you through the process of creating a hotel management booking system using spring boot

= What you'll build

You'll build a service that will accept HTTP GET and HTTP POST

----
HTTP GET message which will retrieve any bookings for a specific customer ID
----
http://localhost:8080/bookingDetails/customer/{customerId}
----
the customerId  is a path variable and used in querying booking details. Json Response contains booking details along with customer 
and room details. The response to an HTTP `GET` request for  `bookingDetails/customer/311` might look like:
+
[source,json] 
----
include::{book-root}/snippets/simple-json-response.txt[]
-
HTTP GET message which will retrieve any bookings for a specific room ID : 
--
http://localhost:8080/bookingDetails/room/{roomId}
+
[source,json] 
----
include::{book-root}/snippets/simple-json-response.txt[]
-----
--
HTTP GET message to determine the availability of a specific room during a given date range
--
http://localhost:8080/bookingDetails/availableRooms/{from_date}/{to_date}

---
HTTP POST message to create a new booking. *HTTP 'POST' call to `/bookingDetails/createBooking` will *add*, or *append*, the enclosed body to the collection (database, filesystem, etc) designated by the booking system.
--
http://localhost:8080/bookingDetails/createBooking
----

[[initial]]
--- Building a REST service
First you need to create Model classes which represents the Json Response. Provide a plain old java object with fields, constructors, and accessors for the required fields i.e customerId, RoomId, RoomType, Customer firstname , lastname , email, booking from and to dates etc.
Booking, customer and room  table relationship is modeled using JPA and Spring Data JPA repositories

Model:
'/src/main/java/com/lochside/hotel/booking/model/Customer.java'

- Each Customer can have more than one bookings. This is a 1:N relationship . The code for the `Booking` entity is shown below:

/src/main/java/com/lochside/hotel/booking/model/Booking.java

=similarly 
'/src/main/java/c'om/lochside/hotel/booking/model/Room.java'
Each room  may have multiple bookings based on the arrival and departure dates. This is again 1:N relationship


We'll use http://spring.io/guides/gs/accessing-data-jpa/[two Spring Data JPA repositories] to handle the tedious database interactions. Spring Data repositories are typically interfaces with methods supporting reading, updating, deleting, and creating records against a backend data store. Some  repositories also typically support data paging, and sorting, where appropriate. 

One repository will manage our `Booking` entities, called `BookingRepository`, shown below. One custom finder-method, `findBookingByCustomerId`, will, _basically_, create a JPA query of the form `select a from Booking a where a.customerId = :customerId`, run it (passing in the method argument `customerId` as a named parameter for the query), and return the results for us. Similarlry there are couple of other methods which will manage retrieving booking details by room Id and list of available rooms for the given date range.

/src/main/java/com/lochside/hotel/booking/repository/BookingRepository.java

`/src/main/java/com/lochside/hotel/booking/repository/BookingRepository.java`
[source,java] 
----
include::{book-root}/src/main/java/com/lochside/hotel/booking/repository/BookingRepository[]
----

Here's the repository for working with `Customer` entities.

/src/main/java/com/lochside/hotel/booking/repository/CustomerRepository.java

`/src/main/java/com/lochside/hotel/booking/repository/CustomerRepository.java`
[source,java] 
----
include::{book-root}/src/main/java/com/lochside/hotel/booking/repository/CustomerRepository[]
----

NOTE: As you see in steps below, Spring uses the http://wiki.fasterxml.com/JacksonHome[Jackson JSON] library to automatically marshal instances of type `Booking` into JSON.

Next you create the resource controller that will serve all the booking requests
	
== Create a resource controller

In Spring's approach to building RESTful web services, HTTP requests are handled by a controller. These components are easily identified by the http://docs.spring.io/spring/docs/{spring_version}/javadoc-api/org/springframework/web/bind/annotation/RestController.html[`@RestController`] annotation, and the `HotelBookingController` for `/bookingDetails/room/{room_id}` by returning a new instance of the `Booking` class:

`/src/main/java/com/lochside/hotel/booking/controller/HotelBookingController.java`
[source,java]
----
include::/src/main/java/com/lochside/hotel/booking/controller/HotelBookingController.java[]
----
This controller is responsible for GET and POST requests. 

The `@RequestMapping` annotation ensures that HTTP requests to `/room/{room_id}` are mapped to the `getBookingsByRoom()` method.

====Next you create the Service class which will serve all the repository requests

`src/main/java/com/lochside/hotel/booking/service/HotelBookingService.java`
[source,java]
----
include::src/main/java/com/lochside/hotel/booking/service/HotelBookingService.java[]
----

This code uses Spring 4's new http://docs.spring.io/spring/docs/{spring_version}/javadoc-api/org/springframework/web/bind/annotation/RestController.html[`@RestController`] annotation, which marks the class as a controller where every method returns a domain object instead of a view. It's shorthand for `@Controller` and `@ResponseBody` rolled together.

The `Booking` object must be converted to JSON. Thanks to Spring's HTTP message converter support, you don't need to do this conversion manually. Because http://wiki.fasterxml.com/JacksonHome[Jackson 2] is on the classpath, Spring's http://docs.spring.io/spring/docs/{spring_version}/javadoc-api/org/springframework/http/converter/json/MappingJackson2HttpMessageConverter.html[`MappingJackson2HttpMessageConverter`] is automatically chosen to convert the `Booking` instance to JSON.

== Make the application executable

Spring boot application creates a standalone application. You package everything in a single, executable JAR file, driven by a good old Java `main()` method. Along the way, you use Spring's support for embedding the link:/understanding/Tomcat[Tomcat] servlet container as the HTTP runtime, instead of deploying to an external instance.

`src/main/java/com/lochside/hotel/booking/LochSideHotelBookingSystemApplication.java`
[source,java]
----
include::src/main/java/com/lochside/hotel/booking/LochSideHotelBookingSystemApplication.java[]
----
============================*******===========
Let's run our REST service
============================******==============

$ mvn clean spring-boot:run

[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building LochSideHotelBookingSystem 0.0.1-SNAPSHOT
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- maven-clean-plugin:2.6.1:clean (default-clean) @ lochside-booking-system ---
[INFO] Deleting C:\Users\sskaru\Documents\workspace-sts-3.8.3.RELEASE\LochSideHotelBookingSystem\target
[INFO]
[INFO] >>> spring-boot-maven-plugin:1.5.1.RELEASE:run (default-cli) > test-compile @ lochside-booking-system >>>
[INFO]

...........................
  .   ____          _        .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::        (v1.5.1.RELEASE)

2017-03-02 12:16:13.253  INFO 9920 --- [           main] c.l.h.b.HotelBookingSystemApplication    : Starting HotelBookingSystemApplication on LAPTOP-J0IIEDE9 with PID 9920 (C:\Users\sskaru\Documents\workspace-sts-3.8.3.RELEASE\LochSideHotelBookingSystem\target\classes started by sskaru in C:\Users\sskaru\Documents\workspace-sts-3.8.3.RELEASE\LochSideHotelBookingSystem)



-------------------------
Rest webservice is up , Lets test our Webservice.

http://localhost:8080/bookingDetails/customer/311
----
The response sample look like :
+
[source,json] 
----
include::{book-root}/snippets/simple-json-response.txt[]
---

== Conclusion
Congratulations! You've just developed a hotel management system RESTful web service with Spring boot.

