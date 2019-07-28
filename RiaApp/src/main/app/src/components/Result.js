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
import ReactToExcel from "react-html-table-to-excel";
import IconButton from "@material-ui/core/IconButton";
import {ArrowBack} from "@material-ui/icons";

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
        const {graph, graph_lines} = this.props.graph;

        const examination_results = examination_points.map(examination_point =>
            (<ExaminationResult key={examination_point.id} examination_point={examination_point}/>));

        const curve_points = control_curves.map(control_curve =>
            (<ControlCurve key={control_curve.id} control_curve={control_curve}/>));

        const graph_props = graph.map(graph => (<Graph key={graph.id} graph={graph}/>));

        const ng_condition = examination_results.ng === null ?
            <tr>
                <th>probe:</th>
                <th>&#8470;</th>
                <th>cpm</th>
            </tr> :
            <tr>
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
                        <Container>
                            <br/>
                            <Row>
                                <div className={"control-curve-col"}>
                                    <Paper>
                                        <Table>
                                            <thead>
                                            <tr>
                                                <th>point:</th>
                                                <th>cpm:</th>
                                                <th>read:</th>
                                            </tr>
                                            </thead>
                                            {curve_points}
                                        </Table>
                                    </Paper>
                                </div>
                                <Col xs="8" s="8" m="8" lg="7">
                                    <Paper>
                                        <GraphLine key={graph_lines.id} graph_lines={graph_lines} graph={graph}/>
                                    </Paper>
                                    <Paper>
                                        <Table striped>
                                            <thead>
                                            <tr>
                                                <th className={"Cell-Text"}>Correlation:</th>
                                                <th className={"Cell-Text"}>Zero Binding %:</th>
                                                <th className={"Cell-Text"}>Regression.:</th>
                                            </tr>
                                            </thead>
                                            {graph_props}
                                        </Table>
                                    </Paper>
                                </Col>
                            </Row>
                            <br/>
                        </Container>
                    </Paper>
                    <br/>
                    <Paper>
                        <Row className={"Examination-Result-Buttons"}>
                            <IconButton href="/dashboard" className={"Examination-Result-Button-Back"}>
                                <ArrowBack/>
                            </IconButton>
                            <ReactToExcel
                                className="fa fa-download fa-2x"
                                table="file_data"
                                filename="file_data"
                                sheet="sheet 1"
                                buttonText=""
                            />
                        </Row>
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
