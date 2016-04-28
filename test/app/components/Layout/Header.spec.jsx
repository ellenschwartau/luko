import Header from 'components/Layout/Header.jsx'
import React from 'react'
import ReactTestUtils from 'react-addons-test-utils'

describe('Header', function() {

    function render() {
        return ReactTestUtils.renderIntoDocument(<Header title="Test"/>);
    }

    it('should render component', function() {
        expect(render()).toBeDefined();
    })

    it('should display title', function(){
        var header = render(),
            title = ReactTestUtils.findRenderedDOMComponentWithTag(header, "h1");
        expect(title.innerHTML).toEqual("Test");
    })

});
