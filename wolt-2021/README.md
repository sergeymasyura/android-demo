Wolt Demo
==================

A coding assignment from Wolt for the Android developer position.

Implemented by [Sergey Masyura](https://www.linkedin.com/in/masyura/).

### Requirements

Build an app that displays a list of venues for the current location of user. The list should contain a maximum of 15 venues. If the server response has more then use the first 15.

Current location is taken from the input list and changes every 10 seconds (your app should
refresh the list automatically).

Each venue also has “Favorite” action next to it. “Favorite” works as a toggle (true/false) and
changes the icon depending on the state. Your app should remember these states and reapply
them to venues that come from the server again.

API endpoint:
GET https://restaurant-api.wolt.fi/v3/venues?lat=0.0&lon=0.0

Important fields in response (json):

| Path / Key                          | Meaning                  |
| ----------------------------------- | ------------------------ |
| id -> $oid                          | Unique id of the venue   |
| name -> [0] -> value                | Name of the venue        |
| short_description -> [0] -> value   | Description of the venue |
| listimage                           | Image URL for the venue  |

Coordinates (latitude, longitude):
60.170187, 24.930599
60.169418, 24.931618
60.169818, 24.932906
60.170005, 24.935105
60.169108, 24.936210
60.168355, 24.934869
60.167560, 24.932562
60.168254, 24.931532
60.169012, 24.930341
60.170085, 24.929569

### Technical details

* The app is written in kotlin and using MVVM approach based on [Guide to app architecture](https://developer.android.com/jetpack/guide)

* UI is minimalistic for demo purposes and does not provide optimal user experience. Updating list during interaction is not something to use in production (when the user touches the screen to mark favorite, but at same moment list is updated). More detailed error handling and feedback can be taken into account (for example, in case of missing network we can guide an user to check the network connection instead of displaying a generic error message).

* No split into gradle modules was done due to simplicity. In normal app at least data layer would be moved to a standalone module. No dependency injection frameworks were used, but basic service locator implementation.

* An application was primarily tested on Google Pixel 2XL device running Android 11. It may happen to look not optimal on different devices or versions of Android or tablets.

* App does not handle any permissions or user data. App is for demo purposes and not pretending to be production ready.


Following libraries are used in app:

* [Android Jetpack](https://developer.android.com/jetpack/) - a collection of libraries from Google.
* [retrofit](https://github.com/square/retrofit) for network requests.
* [Gson](https://github.com/google/gson) for parsing network responses.
* [coil](https://github.com/coil-kt/coil) to load images from the network.
