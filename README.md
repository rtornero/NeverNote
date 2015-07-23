# NeverNote

# Development steps documentation

1.- Created GitHub repository with Android .gitignore and MIT license as defaults.

2.- Checked test requirements by reading the document.

3.- Studied Evernote SDK dependency and documentation.

4.- Got an API Key and a Sandbox account following Evernote SDK guide.

5.- Created a new Android Studio project with one Activity and Fragment as a basis.

6.- Synchronized this new project with GitHub repository.

7.- Checked Evernote SDK demo application and code to get a general idea about its behavior.

8.- Application class extended to get a global context in which we can initialize the Evernote session singleton. We'll register activity lifecycle callbacks too so the Application can listen to Activity changes and detect when to show the Login process (if Evernote session is not started!).

9.- Created a first approach in structure for authentication process. Using MVP to achieve a better separation between classes and leverage the load of native components as Activities and Fragments. Evernote SDK dependency is added, as well as consumer key and secret for Oauth.

10.- Login button added on LoginFragment. It connects with Evernote account and returns successfully. It is required to register the login callback to the Activity component, I've tried adding it from the Fragment but it didn't work. This will modify a little bit my initial structure and require to send this callback to the Fragment as well. 
