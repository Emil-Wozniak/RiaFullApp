import React, {Component} from 'react';
import {connect} from "react-redux";
import PropTypes from "prop-types";
import {getControlCurve} from "../actions/controlCurveActions"
import Paper from "@material-ui/core/Paper";
import {Col, Row} from "reactstrap";
import ControlCurve from "./models/control_curve/ControlCurve";
import ExaminationResult from "./models/examination_result/ExaminationResult";
import Container from "reactstrap/es/Container";

class Result extends Component {
    constructor() {
        super();
        this.state = {
            errors: {}
        };
    }

    componentDidMount() {
        const {identifier} = this.props.match.params;
        this.props.getControlCurve(identifier)
    }

    componentWillReceiveProps(nextProps) {
        if (nextProps.errors) {
            this.setState({errors: nextProps.errors});
        }
    }

    render() {
        const {examination_points} = this.props.examination_point;
        const {control_curves} = this.props.control_curve;
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
    errors: PropTypes.object.isRequired,
    getControlCurve: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    control_curve: state.control_curve,
    examination_point: state.examination_point,
    getControlCurve
});

export default connect(mapStateToProps, {getControlCurve})(Result);
