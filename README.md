# PlantIdentificationApp
Mobile app for taking plant pictures, identifying them and sharing results with other users

## Setup

### Google Maps api key

Go to [google cloud console](https://console.cloud.google.com/apis/dashboard) enable
`Maps SDK for Android` and get the api key. Add the key to project.

```bash
# create an api_keys.xml
touch app/src/main/res/values/api_keys.xml
```

Add the following:

```bash
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="google_maps_api_key">YOUR_API_KEY</string>
</resources>
```

### Firebase config file

Go to [firebase](https://console.firebase.google.com) create a project, then in `Project Settings` -> `General`
add SHA1 and SHA256 keys for debugging look at [stack overflow](https://stackoverflow.com/questions/27609442/how-to-get-the-sha-1-fingerprint-certificate-in-android-studio-for-debug-mode).
Then download the `google-services.json` file and place it in `app/google-services.json`.


### Plant.ID api key

Go to [Plant Id](https://www.kindwise.com/plant-id), create an account and get the key.
Then add it to [IdentifyRequest.kt](app/src/main/java/com/example/plantidentificationapp/api/request/IdentifyRequest.kt).
