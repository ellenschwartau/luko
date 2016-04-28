import Home from 'components/Home/Home.jsx'
import React from 'react'
import ReactTestUtils from 'react-addons-test-utils'

describe('Home', function() {

    function render() {
        return ReactTestUtils.renderIntoDocument(<Home/>);
    }

    it('should render component', function() {
        var home = render();
        expect(home).toBeDefined();
    })

});
