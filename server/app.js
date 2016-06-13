var React = require('react');
var express = require('express');
var config = require('../config.js');
var path = require('path'); // to support relative paths

var app = express();

// access for static resources
app.use(express.static('public'));

// always render index.html - 404 will be handled clientside
app.get("*", (req, res) => {
    res.sendFile(path.resolve('./app/index.html'));
});
app.listen(config.port, function() {
    console.log('Listening at port %s ...', config.port);
});
