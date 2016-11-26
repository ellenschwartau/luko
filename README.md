# react-example-project

Sample project that works with Docker, Nodejs, React, Fluxible, Webpack and Karma.

## Project Structure

    // "The app" for client and server side rendering
    app/
      components/
        ComponentX/
          ComponentX.jsx    // component logic
          ComponentX.scss   // component styles
      app.js                // react app setup with fluxible
      routes.jsx            // config of the routing
      client.jsx            // routes for the client side

    // static and compiled assets
    public/
      assets/
        client.js           <= webpack(app/client.jsx)
        client.css          <= webpack(app/client.jsx)

    server/
      app.js            // serverside entrypoint, starts the express server
      
    index.html          // clientside entrypoint, includes compiled assets
    ... configuration files

## Startup

To build the container and run it use:

    ./script/start

The application will be available under `localhost:8080`.

Or build an run the application without docker via: 

    npm run build
    npm run start


## Tests

To run the tests use:

    npm run test
