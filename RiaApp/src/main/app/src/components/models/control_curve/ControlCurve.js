import React, {Component} from 'react';
import {Col, Row} from "reactstrap";
import {CheckBoxOutlineBlank, CheckBoxOutlined} from "@material-ui/icons";

class ControlCurve extends Component {
    render() {
        const {control_curve} = this.props;
        const condition = control_curve.flagged === false;
        return (
            <React.Fragment>
                    <Row>
                        <Col/>
                        <Col>{control_curve.position}</Col>
                        <Col>{control_curve.cpm}</Col>
                        <Col>{condition ? <CheckBoxOutlineBlank/> : <CheckBoxOutlined/>}</Col>
                    </Row>
            </React.Fragment>
        );
    }
}

export default ControlCurve;
