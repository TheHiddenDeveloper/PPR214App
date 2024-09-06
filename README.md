# PPR214App
*DroneX PPR-214 Project Readme*

Welcome to DroneX PPR-214 Project
This document serves as a comprehensive guide to the DroneX PPR-214 project, an ambitious undertaking that harmonizes Arduino-based hardware and Kotlin-based Android software. 
The project culminates in the creation of a sophisticated drone system, controlled through a meticulously crafted Android application.
Below, we delve into the intricacies of the project, app features, methodology, and more.

Project Overview

DroneX PPR-214 redefines drone technology by seamlessly merging state-of-the-art hardware and software. 
The Android app, built using Kotlin, coroutines, and a gamut of Android development tools, serves as the central control hub for the drone. 
This collaborative synergy results in an exceptional drone control experience for users.

App Features

1. Onboarding Screen

The user journey commences with an intuitive onboarding screen.
This screen provides a user-friendly introduction to the app's features and functionality, ensuring a smooth and engaging initiation process.
![photo_2023-08-26_15-24-33](https://github.com/TheHiddenDeveloper/PPR214App/assets/139608903/16952624-4e21-4e6c-861f-eb4185f87aa9)

3. Login and Sign Up Authentication using Firebase

Ensuring data security and seamless user access is paramount.
 The app leverages Firebase for robust user authentication. This enables users to create accounts, log in securely, and enjoy unhindered access to the app's suite of features.
![photo_2023-08-26_15-24-47](https://github.com/TheHiddenDeveloper/PPR214App/assets/139608903/1ed988a9-d7b1-436f-b552-e60ca298d769)
![photo_2023-08-26_15-24-54](https://github.com/TheHiddenDeveloper/PPR214App/assets/139608903/8e2e6b83-a2cd-4c00-b801-ec5bbdfad51c)
![photo_2023-08-26_15-24-59](https://github.com/TheHiddenDeveloper/PPR214App/assets/139608903/51b1f458-1e69-4de9-9f9c-3f612a9ae2b5)
![photo_2023-08-26_15-25-07](https://github.com/TheHiddenDeveloper/PPR214App/assets/139608903/581ef6bc-8dbe-41b4-824c-3782ccbf8ef7)

5. Permission Access
   
The app seamlessly integrates with Android's permission system, guaranteeing the necessary access rights for critical functionalities such as GPS, Bluetooth, and network communication.
 This integration streamlines the connection between the drone and the user's device.
![photo_2023-06-30_16-46-11](https://github.com/TheHiddenDeveloper/PPR214App/assets/139608903/4fad9688-d393-4100-a82b-7981c10ecc10)


7. Map API Integration
   
Navigation and real-time drone tracking are elevated through the seamless integration of a powerful map API.
Users can visualize the drone's location in real-time, enhancing navigation, flight path monitoring, and overall control precision.

9. Controller UI for Drone Navigation and Use
    
At the core of the app's brilliance lies its user-centric controller UI.
This interface empowers users, both novices and experts, with precise control over the drone's navigation and operational aspects.
The UI's ergonomic design simplifies drone piloting, making it accessible to all.

![photo_2023-08-26_15-25-12](https://github.com/TheHiddenDeveloper/PPR214App/assets/139608903/ceca39ec-f9ba-4ba3-bbee-6d1462d51e5b)


Methodology:

DroneX PPR-214 achieves its technical marvel through the adept use of RF communication, facilitated by the HC-05 Bluetooth module.
This module serves as the conduit for seamless communication between the Android app and the drone itself. 
The app transmits pre-defined commands and values, written in Kotlin, ensuring efficient and effective drone control.
The drone's hardware framework comprises several pivotal components, each contributing to its capabilities:

GPS Sensor: The GPS sensor assumes a central role in providing accurate and real-time location data.
This data is indispensable for real-time mapping, navigation, and executing precise flight maneuvers.

ESP32: The incorporation of the ESP32 module imparts advanced communication capabilities and computational prowess to the drone. 
This enhancement augments its ability to process data and establish sophisticated communication links.

Conclusion:

In summation, the DroneX PPR-214 project exemplifies the harmonious convergence of hardware ingenuity and software artistry.
The Android app, a masterpiece forged with Kotlin and fortified by coroutines and diverse Android development systems, emerges as a testament to user-centric design for controlling drone systems.
.
