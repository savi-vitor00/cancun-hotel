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


#### API Usage and full payloads information can be found here:
- First, run the app. Then, go to [](http://localhost:8080/swagger-ui/)

#### API Structure:
- Initialize Hotel Controller -> As we're using H2 Database with "hibernate.ddl-auto:drop/create", this endpoint is required everytime the application is started/restarted
  - ![image](https://user-images.githubusercontent.com/53449344/178472792-cdb8e98b-7887-4d5c-9a6e-8780625e462e.png)
  - ![image](https://user-images.githubusercontent.com/53449344/178473163-045f393f-eb15-4d45-972c-d31312bfeebf.png)

 
- Checking Controller -> Has three different endpoints for availability searchs:
  - ![image](https://user-images.githubusercontent.com/53449344/178473404-dd04e457-6229-4c8f-85af-7192df1d113d.png)


- Booking Controller -> Responsable for new bookings, as also listing all bookings and bookings by user:
  - ![image](https://user-images.githubusercontent.com/53449344/178473950-dde6c4cd-9fde-4cbf-8667-3e542871ed78.png)


- Modifying Controller -> Responsable for modifying the bookings/stays:
  - ![image](https://user-images.githubusercontent.com/53449344/178474138-630aff1e-6c51-4059-8a28-4acce7d9bf7a.png)


- Canceling Controller -> Responsable for canceling bookings by id, and every booking by user:
  - ![image](https://user-images.githubusercontent.com/53449344/178474657-f25c3065-32f9-434c-bdd0-204e9e0486bc.png)


- All controllers and services are 100% covered by tests.
  - ![image](https://user-images.githubusercontent.com/53449344/178476358-4d6136d5-c0c9-4f2c-ad17-5901d7cda01e.png)
  - ![image](https://user-images.githubusercontent.com/53449344/178476396-304e55b4-530c-4672-bf71-4159b35c30da.png)


