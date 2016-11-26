import React from 'react'
import Header from './Header.jsx'
import Footer from './Footer.jsx'
import './Layout.scss'

/**
 * This component defines the basic Layout consisting of header, content and footer.
 */
class Layout extends React.Component {

    render() {
        // children-property will be set by react router
        return (
            <div>
                <Header title="React Example Project"/>
                <div id="content">
                    {this.props.children}
                </div>
                <Footer/>
            </div>
        );
    }
}

export default Layout
