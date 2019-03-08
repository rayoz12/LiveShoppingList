# Live shopping List
An app that enables collaborative real time editing of shopping lists. 

A rest api belongs with this too and is coming soon.

# Libraries Used:
- OkHttp: Communicate with a RESTFul API to get the list of items
- Gson: Parse JSON from the API.

# The technical details

The app queries the server on user demand. The server is using an 
extermely light disk-based database through json. The server provides 
api endpoints to access and edit this DB.

# API Key
As an added layer of secruity, the server expect an API key. The app uses this key and it's configure through gradle as a build option
In your `~/.gradle/` folder, place the API key in the gradle.properties file
like: `APIAccessKey="<key>"`