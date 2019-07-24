import React, {Component} from 'react';
import PropTypes from "prop-types";
import {Col, Row} from "reactstrap";
import {Link} from "react-router-dom";

class ExaminationPoint extends Component {
    render() {
        const {examination_point} = this.props;
        const filename = (examination_point.probeNumber === 25) ? examination_point.identifier : "";
        const position = examination_point.probeNumber === 25 ? examination_point.position : "";
        const pattern = examination_point.probeNumber === 25 ? examination_point.pattern : "";

        return (
            <React.Fragment>
                <Row>
                    <Col>{position}</Col>
                    <Col><Link to={`${examination_point.identifier}`}>{filename}</Link></Col>
                    <Col>{pattern}</Col>
                </Row>
            </React.Fragment>
        );
    }
}

ExaminationPoint.propTyes = {
    classes: PropTypes.object.isRequired
};

export default ExaminationPoint;
