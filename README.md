# Project Setup Guide

## Prerequisites
To build and run this project, ensure you have the following:
- **Android Studio Koala**
- **Gradle JDK:** JetBrains Runtime 17.0.11

## Setup Instructions

### 1️⃣ Configure `local.properties`
You need to create a `local.properties` file in the root folder of the project and define the path to your Android SDK:
```properties
sdk.dir=/Users/user/Library/Android/sdk
```
Replace `/Users/user/Library/Android/sdk` with the actual path to your Android SDK directory.

### 2️⃣ Generate Google Maps API Key
To use Google Maps and Places API, follow these steps:
1. Go to [Google Cloud Console](https://developers.google.com/maps/documentation/places/android-sdk/cloud-setup).
2. Enable **Maps API** and **Places API**.
3. Generate an API key.
4. Copy the generated API key.

### 3️⃣ Configure `local.defaults.properties`
Create a file named `local.defaults.properties` in the root folder of the project `(if it does not exist already)` and add the following:
```properties
MAPS_API_KEY=YOUR-API-KEY
```
Replace `YOUR-API-KEY` with the API key you generated in the previous step.

## Running the Project
Once the setup is complete, you can build and run the project in Android Studio Koala without issues.

For any issues, check the official documentation or reach out for support!

