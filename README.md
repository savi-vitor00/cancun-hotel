# Booking Management API for cancun-hotel

### What to expect from the API:
- 1 - Initialize room and default customers
- 2 - Check available dates for booking in
- 3 - Insert and list bookings
- 4 - Modify bookings
- 5 - Cancel bookings

#### Booking rules:
- The hotel has only one room available.
- the stay can’t be longer than 3 days.
- the stay can’t be reserved more than 30 days in advance.
-  All reservations start at least the next day of booking.

#### API Structure:
- InitializeController -> As we're using H2 Database with "hibernate.ddl-auto:drop/create", this endpoint is required everytime the application is started/restarted
  - ![image](https://user-images.githubusercontent.com/53449344/178472792-cdb8e98b-7887-4d5c-9a6e-8780625e462e.png)
 
- CheckingController -> Has three different responses for the same purpose:
  - 1 - Returns an UnavailablePeriodsVO, which has the occupied dates for each costumer, or a "No booking was found." when none exists.
  - 2 - A simpler methods with 2 differents String responses; "No booking was found for the given period." and "The room is already booked at given period.".
  - 
