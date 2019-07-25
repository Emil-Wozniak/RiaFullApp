import React, {Component} from 'react';
import {connect} from "react-redux";
import PropTypes from "prop-types";
import {getControlCurve} from "../actions/controlCurveActions"
import ExaminationPoint from "./examinationPoint/ExaminationPoint";
import Paper from "@material-ui/core/Paper";
import {Col} from "reactstrap";

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
                <Col>File Content:</Col>
                <Paper>
                    <Col>
                        {examination_points.map(examination_point =>
                            (<ExaminationPoint key={examination_point.id}
                                               examination_point={examination_point}/>))}
                    </Col>
                    {control_curves.map(control_curve => (
                        <p>{control_curve.identifier}</p>))
                    }
                </Paper>
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
