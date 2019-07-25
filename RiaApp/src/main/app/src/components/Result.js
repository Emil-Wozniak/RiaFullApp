import React, {Component} from 'react';
import {connect} from "react-redux";
import PropTypes from "prop-types";
import {getControlCurve} from "../actions/controlCurveActions"
import {Col, Container, Row, Table} from "reactstrap";
import Paper from "@material-ui/core/Paper";
import ControlCurve from "./models/control_curve/ControlCurve";
import ExaminationResult from "./models/examination_result/ExaminationResult";
import {getGraph, getGraphLines} from "../actions/graphActions";
import Graph from "./models/graph/Graph";
import GraphLine from "./models/graph/GraphLine";
import {getExaminationPoints} from "../actions/examinationPointActions";

class Result extends Component {
    constructor(props) {
        super(props);
        this.state = {
            errors: {}
        };
    }

    componentDidMount() {
        const {identifier} = this.props.match.params;
        this.props.getControlCurve(identifier);
        this.props.getGraph(identifier);
        this.props.getGraphLines(identifier);
        this.props.getExaminationPoints()
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

        const examination_results = examination_points.map(examination_point =>
            (<ExaminationResult key={examination_point.id} examination_point={examination_point}/>));

        const ng_condition = examination_results.ng === null ?
            <tr>
                <th>filename:</th>
                <th>probe:</th>
                <th>&#8470;</th>
                <th>cpm</th>
            </tr> :
            <tr>
                <th>file name:</th>
                <th>probe:</th>
                <th>&#8470;</th>
                <th>cpm</th>
                <th>ng</th>
                <th>x&#772;</th>
            </tr>;

        return (
            <React.Fragment>

                <Container>
                    <br/>
                    <Paper>
                        <br/>
                        <Col>Control Curve:</Col>
                        <hr/>
                        <Row>
                            <Col xs="4" s="4" m="4" lg="3">
                                {control_curves.map(control_curve =>
                                    (<ControlCurve key={control_curve.id} control_curve={control_curve}/>))}
                            </Col>
                            <Col xs="8" s="8" m="8" lg="7">
                                <Paper>
                                    <GraphLine key={graph_lines.id} graph_lines={graph_lines}/>
                                </Paper>
                                <Paper>
                                    <hr/>
                                    {graph.map(graph => (<Graph key={graph.id} graph={graph}/>))}
                                </Paper>
                            </Col>
                        </Row>
                        <br/>
                    </Paper>
                    <br/>
                    <Paper>
                        <Table striped id="file_data">
                            <thead>
                            {ng_condition}
                            </thead>
                            {examination_results}
                        </Table>
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
    getGraphLines,
    getExaminationPoints
});

export default connect(mapStateToProps, {getControlCurve, getGraph, getGraphLines, getExaminationPoints})(Result);
