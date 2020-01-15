# android-weather-app
Weather application
## Summary
    The application has 2 screens. On launch it shows an overview screen (which 
    contains current weather condition and a list of forecasts). When 1 of the list item
    is selected the app navigates to the next screen which shows some details about the forecast.
## DataModels
### DTO objects
    These are the data classes fetched from the REST API and are in the _network_ package.
### Entity objects
    These are the classes stored in RoomDb and are in _database_ package.
### Model
    These are the classes used by the views and the application.
    
    After fetching DTOs they are mapped to Entities for storage.
    Database queries return Entities mapped to models.
    
    
## Architecture
    - Navigation Component
    - Room Db To store fetched weather forecast
    - ViewModel for each screens 
    - Repository which acts as data source for the ViewModels and manages the local/network data
    - Rest Api calls made by RxJava
    
### Repository pattern
On launching the app shows data from the RoomDb, however a refresh is triggered, which makes a
rest API call in background. 
Once network data is fetched the database entries are updated. Since the database returns these
entities as _LiveData_ objects, the viewModels and subsequently  the views are also updated.