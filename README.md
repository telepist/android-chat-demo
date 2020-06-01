### Description
A sample chat application where users can create chat rooms and chat with each other in different chat rooms.

### Technologies
The application has been implemented with Kotlin using the model-view-viewmodel (MVVM) architecture pattern using a set of [Android Jetpack](https://developer.android.com/jetpack/docs/guide) libraries, e.g.
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel)
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata)
- [Data Binding](https://developer.android.com/topic/libraries/data-binding)
- [Navigation](https://developer.android.com/topic/libraries/architecture/navigation)

The application uses [Firebase](https://firebase.google.com/) [authentication](https://firebase.google.com/docs/auth) and [Cloud Firestore](https://firebase.google.com/docs/firestore) as a backend database.
Additionally the application uses following libraries for it's advantage:
- [Koin](https://insert-koin.io/) for dependency injection
- [RxJava2](https://github.com/ReactiveX/RxJava/tree/2.x) for the repository API
- [RxFirebase](https://github.com/FrangSierra/RxFirebase) for using Firebase with RxJava

### Features
#### Supported features
- Logging in with an email & password account
- Entering a nick name in first login
- Creating new chat rooms
- Sending & receiving messages in the chat rooms

#### Missing MVP features / To do
- Registering a new account
- Remember me
- Changing the nick name
- Changing the chat room names
- More authentication methods
- Deleting (archiving) chat rooms
- Deleting chat messages

#### Other improvements needed
- At the moment the full list of chat room messages are passed in every update -> Passing only new messages.
- Mocked repository for development & testing purposes
- More unit tests (view & repository)
- Cucumber UI automation tests
- Generating the chat message timestamps in the backend
- Clearer API definitions between the views and view-models

### Other
This git repository doesn't include the `google-services.json` for accessing the Firebase services. That can be provided on a need basis.


