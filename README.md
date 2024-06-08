#  SweatSketch: Sketch, Sweat, and Succeed. Shared Kotlin Multiplatform Library
The SweatSketch: Sketch, Sweat, and Succeed is a lightweight app for taking gym notes about workout programs and weight-lifting results.

This is a Kotlin Multiplatform library to support my iOS application, SweatSketch.

## Features
The library has the following feature groups:
- The first covers login and sign-up; it encapsulates \/auth\/login, \/auth\/refresh-token, and \/user routes. The access token received with \/auth\/* calls is stored in memory; once it expires, the library calls for a new one. The refresh token is saved in secure storage (Apple Keychain and Android secure storage).
- The second group encapsulates '/user/profile' calls to create, read, or update the user profiles.
- The last one is about exporting and importing workouts and generating links for plan sharing.

## Token Management
When the app starts, it should check whether a refresh token is stored. If there is one, we will try to get a new access token with a dedicated call. If the access token update call is successful, we also update the refresh token in storage, since with the '/auth/refresh-token' call, the server generates a new pair and deletes the old refresh token.
We should use an access token each time we call user profile or workout management endpoints. Before calling endpoints with authentication, we check the ‘expiresIn’ attribute of the access token, and if it’s outdated, we request a new pair with a saved refresh token.

There isn’t any automatic fetch of access tokens. They are only retrieved when the app starts or when calls with authentication are initiated.