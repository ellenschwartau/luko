import React from 'react'

/**
 * Component that should be displayed for not known urls.
 */
export default class NotFound extends React.Component {
    render() {
        return (
            <div>
                <h2>Not Found :(</h2>
                <p>Sorry, the page you requested does not exist.</p>
            </div>
        );
    }
}

// causes the router to return 404
NotFound.isNotFound = true;
