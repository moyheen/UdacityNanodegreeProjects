#Capstone, Stage 2 - Build
This is the final project submitted for the Nanodegree. I took the skills that I learned across my Nanodegree journey and applied it to an app idea of my own. With my approved Stage 1 design and build plan in-hand, I executed on my vision and built my app in Stage 2.

My Uber Client Id, Batch API Key and GCM Sender id which are all located in the Strings.xml file have been removed. To save you the stress of creating new accounts to get those keys, I built the project when the keys were still present. The .apk-release file is located in the root folder of this repository, just in case you only want a quick trial of the app.

GDG NG Events was created for Google Developer Groups (GDGs) in Nigeria to help with managing their events. There are some terms to take note off before using the app. 

* Google Developer Groups (GDGs) are for developers who are interested in Google's developer technology; everything from the Android, Chrome, Drive, and Google Cloud platforms, to product APIs like the Cast API, Maps API, and YouTube API.
* A GDG Champion is the head of the GDG organizers in any country, Nigeria, in this case. 
* In GDG NG Events, the GDG champion acts as the super-admin. He is the only one capable of adding GDG Organizers to the app.
* The GDG Organizers are the admins in this app. Only verified GDG Organizers who have been added to the app by the GDG Champion can create events for their respective chapters.
* The sections for the GDG Champion and Organizers are located in the overflow menu item.
* The GDG Champion doesn't need to enter a username. The password for now is **champion** and it will be changed after the Udacity Evaluation.
* The GDG Organizers have to enter a username and a password. Dummy accounts have been created for two organizers. These two dummy accounts are already pre-populated with dummy events.
   1. email: dft@gmail.com, password: GDG Lagos
   2. email: buk@gmail.com, password: GDG BUK
* The last set of users are the ones who can select any GDG and see the associated events. They have the option of updating their intially selected group which automatically changes the displayed events. They can also save any event locally.
* Firebase was used as the backend for this project.
* Notifications were implemented with the Batch SDK in collaboration with the Firebase backend, and the users can share events by clicking on the Share icon on the event details page.
