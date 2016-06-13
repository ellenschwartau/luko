var React = require('react');
var express = require('express');
var path = require('path'); // to support relative paths
var RequestService = require('../app/services/RequestService.js');


module.exports = function (config) {

    var app = express();

    // access for static resources
    app.use(express.static('public'));

    // request statistical data for search and jobs
    app.get("/statistics", (req, res) => {
        RequestService.execute('get', config.statisticsProviderEndpoint, req.body, req.query, res);
    })

    // always render index.html - 404 will be handled clientside
    app.get("*", (req, res) => {
        res.sendFile(path.resolve('./app/index.html'));
    });

    return app;
}
