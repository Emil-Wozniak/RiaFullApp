import React, {Component} from 'react';
import {connect} from "react-redux";
import PropTypes from "prop-types";
import {getControlCurve} from "../actions/controlCurveActions"
import {Col, Container, Row} from "reactstrap";
import Paper from "@material-ui/core/Paper";
import ControlCurve from "./models/control_curve/ControlCurve";
import ExaminationResult from "./models/examination_result/ExaminationResult";
import {getGraph, getGraphLines} from "../actions/graphActions";
import Graph from "./models/graph/Graph";
import GraphLine from "./models/graph/GraphLine";

class Result extends Component {
    constructor() {
        super();
        this.state = {
            errors: {}
        };
    }

    componentDidMount() {
        const {identifier} = this.props.match.params;
        this.props.getControlCurve(identifier);
        this.props.getGraph(identifier);
        this.props.getGraphLines(identifier);
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.errors) {
            this.setState({errors: nextProps.errors});
        }
    }

    render() {
        const {examination_points} = this.props.examination_point;
        const {control_curves} = this.props.control_curve;
        const {graph} = this.props.graph;
        const {graph_lines} = this.props.graph;

        return (
            <React.Fragment>
                <Container>
                    <br/>
                    <Paper>
                        <br/>
                        <Col>Control Curve:</Col>
                        <hr/>
                        <Row>
                            <Col xs="4" s="4" m="4" lg="4">
                                {control_curves.map(control_curve =>
                                    (<ControlCurve key={control_curve.id}
                                                   control_curve={control_curve}/>))}
                            </Col>
                            <Paper>
                                <Col xs="6" s="6" m="6" lg="6">
                                    <GraphLine key={graph_lines.id} graph_lines={graph_lines}/>
                                    {graph.map(graph => (<Graph key={graph.id} graph={graph}/>))}
                                </Col>
                            </Paper>
                        </Row>
                        <br/>
                    </Paper>
                    <br/>
                    <Paper>
                        <br/>
                        <Row>
                            <Col>filename</Col>
                            <Col>position</Col>
                            <Col>probe number</Col>
                            <Col>cpm</Col>
                            <Col>ng</Col>
                            <Col>flagged</Col>
                        </Row>
                        <hr/>
                        <Row>
                            <Col>
                                {examination_points.map(examination_point =>
                                    (<ExaminationResult key={examination_point.id}
                                                        examination_point={examination_point}/>))}
                            </Col>
                        </Row>
                    </Paper>
                </Container>
            </React.Fragment>
        );
    }
}

Result.propTypes = {
    control_curve: PropTypes.object.isRequired,
    examination_point: PropTypes.object.isRequired,
    graph: PropTypes.object.isRequired,
    errors: PropTypes.object.isRequired,
    getControlCurve: PropTypes.func.isRequired,
    getGraph: PropTypes.func.isRequired,
    getGraphLines: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    control_curve: state.control_curve,
    examination_point: state.examination_point,
    graph: state.graph,
    getControlCurve,
    getGraph,
    getGraphLines
});

export default connect(mapStateToProps, {getControlCurve, getGraph, getGraphLines})(Result);
