import React, {Component} from 'react';
import {Col} from "reactstrap";

class Graph extends Component {
    render() {
        const {graph} = this.props;

        return (
            <React.Fragment>
                <tbody>
                <tr>
                    <td className={"Control-Curve-Font"}><Col> {graph.correlation}</Col></td>
                    <td className={"Control-Curve-Font"}><Col>  {graph.zeroBindingPercent}</Col></td>
                    <td className={"Control-Curve-Font"}><Col> {graph.regressionParameterB}</Col></td>
                </tr>
                </tbody>
            </React.Fragment>

        );
    }
}

export default Graph;
