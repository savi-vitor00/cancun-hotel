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
  - ![image](https://user-images.githubusercontent.com/53449344/178473163-045f393f-eb15-4d45-972c-d31312bfeebf.png)

 
- CheckingController -> Has three different responses for bookings searchs:
  - ![image](https://user-images.githubusercontent.com/53449344/178473404-dd04e457-6229-4c8f-85af-7192df1d113d.png)
