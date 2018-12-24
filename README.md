# Weather-App
Weather App

I opted for MVVM architecture model in this application just to separate the user interface logic from the backend and keep the UI code simple and free of business logic to make it easier to manage.

To better manage the life cycle state of application components (activities, fragments, objects), I used LiveData as an observable data holder. This allows application components to be able to observe changes to objects without creating explicit dependencies.

Android Room for persistence. This makes it easier to work with the SQLiteDatabase objects in the application, by decreasing the amount of standard code and checking SQL queries at compile time.

The main fragment displays a RecyclerView that contains CardViews with SwipeToDelete and PullToRefresh data from server.
