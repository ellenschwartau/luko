# base image for node, does also include npm
FROM node:5.0

# author
MAINTAINER Ellen Schwartau

# Dockerfile to build the image for the frontend
CMD echo 'Building docker image for frontend'

# install dependencies (withou devDependencies)
ADD package.json package.json
RUN npm install --production

# copy files
ADD app/     app/
ADD public/ public/
ADD server/  server/
ADD config.js config.js
ADD index.html index.html

# need CMD shell syntax for variable substitution
CMD node server/app.js

# specify the port the container listens on at runtime
EXPOSE 8080
