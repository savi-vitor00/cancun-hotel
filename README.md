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
- CheckingController -> Has three different responses for the same purpose:
  - 1 - Returns and UnavailablePeriodsVO, which has the occupied dates for each costumer, or a "No booking was found." when none exists.
