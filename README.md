# Guestlogix Take Home Test - Mobile

At Guestlogix we feel that putting developers on the spot with advanced algorithmic puzzles doesn’t exactly highlight one’s true skillset. The intention of this assessment is to see how you approach and tackle a problem in the real world, not quivering in front of a whiteboard.

### What is the test?

You will be building a mobile application to draw a route on a map between two (or more) airports. Included in this repository is a set of Airport, Airline, and Route data. Your task is to provide the user with a form to enter the origin and destination airports and display a route that connects them (if any). If a route between the origin and destination includes more than one stopover, the line drawn on the map must go through all airports in order.

### User Stories

- As a user I can enter IATA codes of an origin and a destination airport and view a path between the two on a map. Airports in the data set with a null IATA code are provided for the sake of completeness, and may be omitted.
- As a user I can enter IATA codes of an origin and destination airport that are not connected by a direct route. In this case, the route drawn on the map must show the shortest possible travel path between the two airports, going through all airports visited along the way in order. For the sake of simplicity, the shortest path is defined as the one with the least transfer (ie. it will take the same amount of time to travel between two airports, regardless of the physical distance between them). Keep in mind that an indirect route can go through more than one transfer airport before it reaches its final destination.
- As a user I am provided meaningful feedback should no route exist between the airports.
- As a user I am provided meaningful feedback if information entered is incorrect.

### Requirements

The application may be done in Xamarin or in any native language that runs on the Android or iOS platforms. Otherwise, you have complete freedom in terms of how you implement the solution, as long as all user requirements are met.

### Submitting

1. Fork this repository and provide your solution.
2. Run through it one last time to make sure it works!
3. Send an email to indicate that you have completed the challenge. 

### Questions

If you have any questions during the challenge feel free to email Peter Samsonov at psamsonov@guestlogix.com. Whether it be a question about the requirements, submitting, anything, just send the email!

### Assumptions

- routes.csv contains airline id 'AC', which is not in airlines.csv.  From Peter's email, it is assumed that the routes do not need to be filtered out.
- airlines.csv is not being used and no code is created to use it
- airports.csv contains IATA 3 with value \N.  These are assumed to be invalid airport data and will not be used.
- routes with multiple airline servicing it will not be differentiated.
- it is not necessary to mark the origin, transfer airport #, and the destination in the map.  However, the name and the IATA are included in the marker.
- User will wait until the map is loaded before using the app.  No splash screen, progress bar, etc will prevent the user from using the app before the map and data are fully loaded.

### Installation

This application uses google map.  Therefore, you need to enter your own google_maps_key in google_maps_api.xml in order to run the release version.

A restricted key is included in the debug version.  You can build and install the debug version by running

```sh
gradlew installDebug in the terminal.
```