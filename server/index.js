console.log('Running in %s env.', process.env.NODE_ENV);
var config = require('../config/configProvider.js')(process.env.NODE_ENV);

// app server
var app = require('./app')(config)
app.listen(config.appPort, function() {
    console.log('APP listening at port %s ...', config.appPort);
});

// admin server
var admin = require('./admin')(config)
admin.listen(config.adminPort, function() {
    console.log('ADMIN listening at port %s ...', config.adminPort);
});
