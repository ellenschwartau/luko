import Layout from 'components/Layout/Layout.jsx'
import React from 'react'
import ReactTestUtils from 'react-addons-test-utils'

describe('Layout', function() {

    function render() {
        return ReactTestUtils.renderIntoDocument(<Layout/>);
    }

    it('should render component', function() {
        expect(render()).toBeDefined();
    })

});
