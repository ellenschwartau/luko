import NotFound from 'components/NotFound/NotFound.jsx'
import React from 'react'
import ReactTestUtils from 'react-addons-test-utils'

describe('NotFound', function() {

    function render() {
        return ReactTestUtils.renderIntoDocument(<NotFound/>);
    }

    it('should render component', function() {
        expect(render()).toBeDefined();
    })

});
