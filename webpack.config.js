// --
// configures webpack to bundle and translate all style sheets and scripts
// --
var webpack = require('webpack');
var path = require('path');
var absPath = function(filename) { return path.resolve(__dirname, filename); };
var ExtractTextPlugin = require('extract-text-webpack-plugin');

module.exports = {
    entry: './app/client.jsx',
    output: {
        path: absPath('public/assets'),
        filename: 'client.js',
        publicPath: '/assets/'
    },
    plugins: [
        // put all css into a client.css file
        new ExtractTextPlugin('client.css')
    ],
    module: {
        loaders: [
            {
                test: /\.*css$/,
                loader: ExtractTextPlugin.extract('style', 'css!sass?sourceMap&omitSourceMapUrl=true')
            },
            {
                test: /\.js(x)?$/,
                loader: 'babel', exclude: /node_modules/
            }
        ]
    }
};
