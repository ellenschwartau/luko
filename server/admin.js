var express = require('express');
var Client = require('prom-client');

module.exports = function (config) {
    var app = express();

    app.get('/ping', function(req, res) {
        res.set({
            'Content-Type': 'text/plain; charset=ISO-8859-1',
            'Cache-Control': 'must-revalidate,no-cache,no-store', // disable cache
            'Pragma': 'no-cache',
            'Expires': 0,
        })
        res.remove
        res.send('pong\n');
    })

    app.get('/metrics', function(req, res, next){
        res.end(Client.register.metrics());
    });

    return app;
}
