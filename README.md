## Demo application for Android Developer position at Amer Sports

The demo application accesses [GitHub GraphQL API v4](https://developer.github.com/v4/) using [Apollo GraphQL Client](https://github.com/apollographql/apollo-android). 

The application is using an MVVM pattern with Data Binding, as well as other [Android Jetpack Components](https://developer.android.com/jetpack/).

From users point of view the app provides following functionality:
* Displays list of commits to the default branch of the github repository.
* Support pull to refresh functionality.
* Automatically loads more commits when reaching the end of the list.
* Opens the commit in a web browser after clicking on a revision hash.
* Allows to expand and collapse a commit message by click on it.
* Keeps the data on orientation change.
* Shows About dialog available via options menu.
* Handles errors during the data loading and displays a notification to the user if any.

The application is not pretending to be perfect since no production use of it.


## Libraries

The application is built upon following libraries:
* [Android Jetpack](https://developer.android.com/jetpack/) - a collection of libraries from Google.
  * [AppCompat](https://developer.android.com/topic/libraries/support-library/packages.html#v7-appcompat)
  * [Data Binding](https://developer.android.com/topic/libraries/data-binding/)
  * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
  * [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
  * [Fragment](https://developer.android.com/guide/components/fragments)
  * [Layout](https://developer.android.com/guide/topics/ui/declaring-layout)
* [RxJava2](https://github.com/ReactiveX/RxJava) - Reactive Extensions for the JVM. 
* [Apollo GraphQL](https://github.com/apollographql/apollo-android) - GraphQL compliant client that generates Java models from GraphQL queries and performs validation.
* [OkHttp3](https://github.com/square/okhttp) - An HTTP client for Android applications.
* [Dagger 2](https://github.com/google/dagger) - dependency injection framework.
* [Glide](https://github.com/bumptech/glide) -  image loading framework for Android.
* [Mockito](https://site.mockito.org/) - a mocking framework for unit tests in Java.


## Possible improvements

* Licenses to be checked for dependencies used, so the app complies with those from legal point of view.
* Javadoc may be considered to be added to document the source code.
* Due to intensive code generation (caused by dependency injections, data bindings and graphql queries) it makes sense to go through all fields and methods and suppress Unused warning for the false positive cases.
* Some optional nice features can be added to the app (i.e. search, branch selection, displaying co-authors, comments, etc), but it must depend on actual use cases and out of the scope for this task.
* Although the app layout responds to different screen sizes, it makes sense to create alternative layouts for tablets. Also landscape and portrait layouts may need to be designer separately in order to provide better user experience.
* Application is using Amer Sports logo which is violation of the trademark.
* Privacy policy is not provided for the app.
* Security to be considered since app requires API token even to access public repositories.
* There is a minor bug, since the commit text view is expandable, it does not preserve its state on orientation change. So if the commit message text view was expanded for the item, on orientation change it will be displayed as collapsed. Also related minor bug, when viewholders are reused, the commit text view does not reset from expanded to default collapsed state. Decided to leave as is, minor priority.
* In case of missing network connectivity, the application can detect the network connectivity change and try to load data automatically.
* In case the application is unable to load data, now app shows generic error message (Something went wrong). The app can be a bit smarter to help the user understand where is issue.
* In order to load more feature working in recycler view, the pagination limit can not be too small (less than 5). Since the feature uses scroll listener, the initial content must be longer than height of the recycler view. 
* GitHub shows rounded images for the avatars, so makes sense to do the same in the app.
* Unit tests are not comprehensive and do not cover whole source code. However they show how to mock a web server responses, test live data or mock classes. UI tests with Expresso are out of scope for this demo.
* Common TextView styles and some dimensions needs to be extracted to styles.xml and dimens.xml
* Corporate material design palette needs to be designed in order to use brand colors (either Amers Sports or GitHub)
* GitHub GraphQL API v4 requires API token in order to work (even for public repositories to authenticate to the API over Basic Authentication). For now application is using my own and it's specified in the build configuration, one day it may be removed, so consider to use own token if needed.
* Two graphql queries used - one for fetching recent commits and another for fetching commits after specified revision. They both return actually same types and format of data, but for now we have to convert both of the separately. Need to consider does it make sense to create more complex but single query or it's better to keep queries separated.
* For now app uses last commit revision in order to load more data, but this query can improved since graphql already provides some mechanisms for pagination. Need to be investigated further.
* Handling of the click on git hash through MutableLiveData from viewmodel to activity can be improved probably.
* There is no persistence layer in the app for now, but normalized disk cache and some others can be enabled in the apollo graphql client.
* Animations to be considered, i.e. when expanding a commit message. The ripple effect can be to the revision hash on click. Also the app bar can be hidden on scrolling down.
* The app needs to be tested on variety of devices with different firmwares, screen sizes and hardware configurations. For now only Nexus 6P and emulator was used during development.
* Release build variant was not tested or built, so ProGuard additional rules may need to be added to do not strip some methods/classes.
* Now if a request to load additional data fails the adapter is cleared and full size error view is displayed. From user experience point of view, the refresh of the data should not cause the loss of already loaded data as we do. If something went wrong and adapter was empty before, it make sense to show full screen error view as it's now, but if an update/load more request failed, it's better to keep existing data and show a temporary snackbar/toast and don't clear data in the adapter.
* Performance analysis needs to be done in terms of memory and network consumptions.

This list is not comprehensive in terms of both coding, UX and others points of view, but enough for demo purposes.


## Author
Sergey Masyura, join my professional network at [LinkedIn](https://www.linkedin.com/in/masyura).


