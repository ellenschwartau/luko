import React from 'react'
import connectToStores from 'fluxible-addons-react/connectToStores'
import StatisticalDataStore from '../../stores/StatisticalDataStore'
import RequestStatisticalDataAction from '../../actions/RequestStatisticalDataAction.jsx'
import StatisticalDataService from '../../services/StatisticalDataService.js'
import './Chart.scss'
import classNames from 'classnames'

import Chartist from 'react-chartist'

/**
 * Component to display charts.
 * @param style specifies the styling
 * @param dataKey key for the data set to be displayed
 * @param labelsKey key for the attribute that should be interpreted as label
 * @param valuesKey key for the attribute that should be interpreted as value
 * @param type type of the chart, e.g. 'Bar'
 */
class Chart extends React.Component {

    componentWillMount(){
        // initialize statistics-data
        this.state = {statistics: {labels: [], series: [[]]}, dataLoaded: false};
        // request the statistical data
        this.context.executeAction(RequestStatisticalDataAction, this.props.dataKey);
    }

    componentWillReceiveProps(nextProps){
        // extract statistical data if underlying data set changes
        this.setState({
            statistics: StatisticalDataService.transform(nextProps.data, nextProps.labelsKey, nextProps.valuesKey),
            dataLoaded: nextProps.data.length > 0
        })
    }

    render() {
        // set boundaries of the chart
        let classes = classNames("chart", {"error": !this.state.dataLoaded})
        var options = {
            high: Math.max.apply(null, this.state.statistics.series[0]),
            low: 0
        };
        return (
            <div className={classes}>
                <p>Keine statistischen Daten für {this.props.dataKey} verfügbar.</p>
                <Chartist className={this.props.style} data={this.state.statistics} options={options} type={this.props.type} />
            </div>
        );
    }
}

// makes the component able to execute an action
Chart.contextTypes = {
    executeAction: React.PropTypes.func.isRequired
}

Chart.propTypes = {
    style: React.PropTypes.string,
    dataKey: React.PropTypes.string,
    labelsKey: React.PropTypes.string,
    valuesKey: React.PropTypes.string,
    type: React.PropTypes.string
}

// binds the properties from inside the store and listens for changes
export default Chart = connectToStores(Chart, [StatisticalDataStore], (stores, props) => ({
    data: stores.getStore(StatisticalDataStore).getStatistics(props.dataKey)
}))

