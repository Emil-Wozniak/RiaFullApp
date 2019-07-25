import React, {Component} from 'react';
import {Col} from "reactstrap";

class Graph extends Component {
    render() {
        const {graph} = this.props;

        return (
            <Col>
                {graph.correlation}
            </Col>
        );
    }
}

export default Graph;
