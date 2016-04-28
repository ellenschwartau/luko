var webpack = require('webpack');
var path = require('path');
var absPath = function(filename) { return path.resolve(__dirname, filename); };
console.log(absPath('app'))

module.exports = function (config) {
    config.set({
        basePath: '',
        frameworks: ['jasmine'],
        files: [
            'karma.webpack.js',
        ],
        preprocessors: {
            'karma.webpack.js': ['webpack']
        },
        webpack: {
            resolve: {
                root: absPath('app'),
            },
            module: {
                loaders: [
                    { test: /\.jsx?$/, loader: 'babel', exclude: /node_modules/},
                    { test: /\.scss$/, loader: 'style!css!sass?sourceMap&omitSourceMapUrl=true' },
                ]
            }
        },
        // test results reporter to use
        reporters: ['dots'],
        // web server port
        port: 30201,
        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: true,
        // start these browsers
        // available browser launchers: https://npmjs.org/browse/keyword/karma-launcher
        // we use phantomJs2 as phantomJs doesn't support function.prototype.bind
        // -> https://github.com/ariya/phantomjs/issues/10522
        browsers: ['PhantomJS', 'Chrome'],
        // Continuous Integration mode
        // if true, Karma captures browsers, runs the tests and exits
        singleRun: false
    });
};
