import React, {Component} from 'react';
import {Container,Col} from "reactstrap";

class Graph extends Component {
    render() {
        const {graph} = this.props;
/*
zeroBindingPercent(pin): 21
regressionParameterB(pin): -1.0575
 */
        return (
            <Container>
                <Col className={"Graph-Font"}>Correlation: {graph.correlation}</Col>
                <Col className={"Graph-Font"}>Zero binding %: {graph.zeroBindingPercent}</Col>
                <Col className={"Graph-Font"}>Regression: {graph.regressionParameterB}</Col>
            </Container>

        );
    }
}

export default Graph;
