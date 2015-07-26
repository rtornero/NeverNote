# NeverNote

# Development steps documentation

1.- Created GitHub repository with Android .gitignore and MIT license as defaults.

2.- Checked test requirements by reading the document.

3.- Studied Evernote SDK dependency and documentation.

4.- Got an API Key and a Sandbox account following Evernote SDK guide.

5.- Created a new Android Studio project with one Activity and Fragment as a basis.

6.- Synchronized this new project with GitHub repository.

7.- Checked Evernote SDK demo application and code to get a general idea about its behavior. It has its own model and factories implemented so our app won't have any specific model, just will take what is needed from the SDK (such as the Note object).

8.- Application class extended to get a global context in which we can initialize the Evernote session singleton. We'll register activity lifecycle callbacks too so the Application can listen to Activity changes and detect when to show the Login process (if Evernote session is not started!).

9.- Created a first approach in structure for authentication process. Using MVP to achieve a better separation between classes and leverage the load of native components as Activities and Fragments. Evernote SDK dependency is added, as well as consumer key and secret for Oauth.

10.- Login button added on LoginFragment. It connects with Evernote account and returns successfully. It is required to register the login callback to the Activity component, I've tried adding it from the Fragment but it didn't work. This will modify a little bit my initial structure and require to send this callback to the Fragment as well. 

11.- Login fragment restores the login button state to enabled when finishing authentication.

12.- Added an activity lifecycle listener to the main Application class. This approach is based on the one implemented for the
Evernote SDK Demo which i liked a lot. This allows to control the Activity flow of the app, and detect whenever there is no session on Evernote singleton to show the Login screen again. I adapted it to my code and fully understood its behavior.

13.- New Activity created that will serve as our main access point to the app. This Activity will contain different Fragments, but for now it only shows the list of notes Fragment. I will update this class with a navigation interface.

14.- Added a Fragment that retrieves the list of notes created by our logged user. To achieve this, I had to retrieve all the notebooks first, as they are their top container, and iterate through each notebook to retrieve a maximum number of 100 notes for each one (this is a limitation of the Evernote SDK, it is intended to be used for pagination when presenting the list of notes included on a notebook). To implement this feature I decided to use a concurrent execution approach in which I start the notes fetching processes one by one and wait until they are finish to notify the interface with the result. I preferred this solution to the serial execution one, as it should be quicker and more readable. This Fragment uses a RecyclerView as view container.

15.- Created a new Fragment that displays the details of a selected note. This Fragment is pushed to a stack so the user can return to the list of notes with back navigation, adapting a master-detail pattern. Notes retrieved when fetching the list don't contain all the required details, just the simple ones, so we need to make a new API call to retrieve the selected note details. This is achieved by passing the Guid (Evernote's identifier for their entities) to the API.

16.- Added a Navigator class to the MainActivity that allows to control its context navigation. Fragments will get a handler to this instance when being attached to the Activity, as the ListFragment does for pushing the new ContentFragment for each of the notes.

17.- RecyclerView doesn't have methods for handling click events by itself, so I have added a new interface implemented by each ViewHolder that exposes both the clicked view and its position to the Adapter.

18.- New DialogFragment added to create new notes. With a title and a content, both entered by text input fields for now, calls Evernote's SDK and notifies the ListFragment to reload data when the process has ended, so the new note appears on it. This note creation process doesn't specify which Notebook should be added to, so for now, the notes are added to the user's default Notebook (the first one). I'll try to add a Spinner to enable Notebook selection, if possible (if no Notebook was created then no Spinner will appear).

19.- Screen orientation changes are now detected by the Activities so the app doesn't crash in middle of some operation or whenever I try to access a parameter set by onAttach(). This is a fix for now, if i have time i will control these events myself to ensure there is no problem at all.
