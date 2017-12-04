# Popular Movies - Stage 2

This is a simple Android Studio project for the [Associate Android Developer Fast Track Nanodegree Program](https://in.udacity.com/course/associate-android-developer-fast-track--nd818) taught by Udacity and Google. The goal from the last Stage 1 was to build an app that presented the user with a grid of movie posters, allowed users to change sort order, and presented a screen with additional information on the movie selected by the user. In this second and las Stage, the new goal is to add additional functionality to the app built in Stage 1:

* Allow users to view and play trailers (either in the youtube app or a web browser).
* Allow users to read reviews of a selected movie.
* Allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request.
* Modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

# Notes

* **You need an API key from The Movie DB to run this app**. I have removed my personal API key from this project by moving it to gradle.properties and adding this file to .gitignore before sharing my project to GitHub, as explained in [this post from Richard Rose's Blog](https://richardroseblog.wordpress.com/2016/05/29/hiding-secret-api-keys-from-git/). You can get information for getting your own free API key from The Movie DB [here](https://www.themoviedb.org/faq/api).
* Besides the Project Specifications below, I have implemented some changes from the original project:
  * I use an AsyncTaskLoader instead of an AsyncTask.
  * I use Preferences for storing the sort order.

# Project Specifications

## Common Project Requirements

* App is written solely in the Java Programming Language.
* App conforms to common standards found in the Android Nanodegree General Project Guidelines.

## User Interface - Layout

* UI contains an element (e.g., a spinner or settings menu) to toggle the sort order of the movies by: most popular, highest rated.
* Movies are displayed in the main layout via a grid of their corresponding movie poster thumbnails.
* UI contains a screen for displaying the details for a selected movie.
* Movie Details layout contains title, release date, movie poster, vote average, and plot synopsis.
* Movie Details layout contains a section for displaying trailer videos and user reviews.

## User Interface - Function

* When a user changes the sort criteria (most popular, highest rated, and favorites) the main view gets updated correctly.
* When a movie poster thumbnail is selected, the movie details screen is launched.
* When a trailer is selected, app uses an Intent to launch the trailer.
* In the movies detail screen, a user can tap a button(for example, a star) to mark it as a Favorite.

## Network API Implementation

* In a background thread, app queries the /movie/popular or /movie/top_rated API for the sort criteria specified in the settings menu.
* App requests for related videos for a selected movie via the /movie/{id}/videos endpoint in a background thread and displays those details when the user selects a movie.
* App requests for user reviews for a selected movie via the /movie/{id}/reviews endpoint in a background thread and displays those details when the user selects a movie.

## Data Persistence

* The titles and ids of the user's favorite movies are stored in a ContentProvider backed by a SQLite database. This ContentProvider is updated whenever the user favorites or unfavorites a movie.
* When the "favorites" setting option is selected, the main view displays the entire favorites collection based on movie ids stored in the ContentProvider.
