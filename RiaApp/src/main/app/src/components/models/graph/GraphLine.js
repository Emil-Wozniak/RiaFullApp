import React, {Component} from 'react';
import Col from "reactstrap/es/Col";

class GraphLine extends Component {

    render() {
        const {graph_lines} = this.props;
        const x_axis = () => graph_lines.map(line => (
            <Col>{line.x}</Col>
        ));
        return (
            <div>
                {x_axis()}
            </div>
        );
    }
}

export default GraphLine;
