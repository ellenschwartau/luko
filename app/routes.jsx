// --
// Specifies the routes to specify which content should be displayed depending on the called url
// --

import React from 'react';
import { Route, IndexRoute } from 'react-router'
import Home from './components/Home/Home.jsx'
import Layout from './components/Layout/Layout.jsx'
import NotFound from './components/NotFound/NotFound.jsx'

module.exports = (
    <Route path="/" component={Layout}>
        <IndexRoute component={Home}/>
        <Route path="*" component={NotFound}/>
    </Route>
);
