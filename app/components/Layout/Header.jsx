import React from 'react'
import './Header.scss'

/**
 * This component represents the header.
 * @param title the title to be displayed
 */
class Header extends React.Component {
    render() {
        return (
            <div id="header">
                <h1>{this.props.title}</h1>
            </div>
        );
    }
}

Header.propTypes = {
    title: React.PropTypes.string
}

export default Header;
