import React, {Component} from 'react';
import {Col, Row} from "reactstrap";
import {CheckBoxOutlineBlank, CheckBoxOutlined} from "@material-ui/icons";

class ControlCurve extends Component {
    render() {
        const {control_curve} = this.props;
        const condition = control_curve.flagged === false;
        return (
            <React.Fragment>
                    <Row className={"Control-Curve-Font"}>
                        <Col className={"Control-Curve-Font"}>{control_curve.position}</Col>
                        <Col className={"Control-Curve-Font"}>{control_curve.cpm}</Col>
                        <div className={"Control-Curve-Font"}>{condition ? <CheckBoxOutlineBlank/> : <CheckBoxOutlined/>}</div>
                    </Row>
            </React.Fragment>
        );
    }
}

export default ControlCurve;
