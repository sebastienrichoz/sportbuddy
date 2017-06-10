# SportBuddy
This project is a simple web application letting you find sport partners by specifying the activity, level and city you target.

TODO screenshot of webapp

**Project under development**

## Technologies
Made with love using the following technos : **TODO versions**
- Scala PlayFramework 
- ScalaJS
- Slick
- MySQL

## Run

### with Intellij
Clone the repo, open it with the IDE and execute the `server/run` config. Then browse at `localhost:9000`.

### with sbt
TODO

## Architecture

How are client and server side communicating. Init process ? => GET /
Server and database queries

### Database modelling
UML + small word about sample data

### Application lifecycle
1. Server creation
2. Database creation and seeding with evolutions
3. Client first GET request to retrieve views and assets (in `server/public/`)
4. Client GET request to retrieve all buddies
5. Client-Server communications depending of user interactions

### Authors
Sébastien Richoz & Damien Rochat

### License
- HTML template from ...
- The rest of the project is under MIT license.
