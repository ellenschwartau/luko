import Footer from 'components/Layout/Footer.jsx'
import React from 'react'
import ReactTestUtils from 'react-addons-test-utils'

describe('Footer', function() {

    function render() {
        return ReactTestUtils.renderIntoDocument(<Footer/>);
    }

    it('should render component', function() {
        expect(render()).toBeDefined();
    })

});
