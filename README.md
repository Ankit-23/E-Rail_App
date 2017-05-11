# E-Rail_App

**Importnant Notes**
- 1.Erail.apk file is included in this project which can be used to directly install this app on the phone.
- 2.It is based on indian railway api, so daily request quota is limited. If the request quota is exceeded then new api key can be used provided in DataClass/ApiKey.
  Just replace string API_KEY with the provided api key list in the same class.

- 3.dependencies required (needed if the app is installed using android studio)
  - compile 'com.android.support:appcompat-v7:25.1.1'
  - compile 'com.android.support:design:25.1.1'
  - compile 'com.android.support:support-v4:25.1.1'
  - compile 'com.android.support:cardview-v7:25.1.1'
  - compile 'com.github.ganfra:material-spinner:1.1.1'
  - compile 'com.android.support:recyclerview-v7:25.1.1'
  - compile 'com.android.volley:volley:1.0.0'
  - compile 'de.hdodenhof:circleimageview:2.1.0'
  
  
-----------------------ABOUT THE APP-----------------------
- E-Rail: An android app to provide the functionalities of indian railways to users on their finger tips. It is based on indian railway api 
which provides various features like checking of live runnung status, seat availability, checking of pnr status etc.

- Functionalities provided by this app:-
  * live status- used to check the live running status of any train.
  * seat availability- used to check the availability of seats in any train on particular date.
  * train between stations- used to get the list of trains between given source and destination.
  * check fare- used to check fare for tickets of any train.
  * pnr status- used to check current pnr status of any valid pnr number.
  * share- used to share the link of the app.
  

- This app is incorporated with features :-
  * Navigation drawer
  * Recyclerview with cardview
  * Fragments
  * DialogFragment
  * DatePicker
  * JSON parsing
  * Railway APIs
  * Expandable listvew
  * Asynchronously network calls using -AsyncTask and volley
  * AlertDialog
  * ProgressDialog

  
    


