import React, {Component} from 'react';
import {CheckBoxOutlineBlank, CheckBoxOutlined} from "@material-ui/icons";
import Col from "reactstrap/es/Col";

class ExaminationResult extends Component {
    render() {
        const {examination_point} = this.props;
        const ng_condition = examination_point.ng === "0.0";
        const filename = (examination_point.identifier !== null) ?
            <Col Cell-Text>{examination_point.identifier}</Col> : "";
        const position = (examination_point.position !== null) ?
            <td className={"Examination-Result-Average"}>{examination_point.position}</td>
            : "";
        const probe_number = (examination_point.probeNumber !== null) ?
            <Col Cell-Text>{examination_point.probeNumber}</Col> : "";
        const cpm = (examination_point.cpm !== null) ?
            <Col Cell-Text>{examination_point.cpm}</Col> : "";
        const ng = (examination_point.ng !== null) ?
            <td style={{backgroundColor: ng_condition ? "#f05545" : "striped"}}>
                <Col Cell-Text> {examination_point.ng}</Col>
            </td>
            : "";
        const condition = examination_point.flagged === false;

        return (
            <React.Fragment>
                <tbody>
                <tr>
                    <td>{filename}</td>
                    {position}
                    <td>{probe_number}</td>
                    <td>{cpm}</td>
                    {ng}
                    <td>{condition ? <CheckBoxOutlineBlank/> : <CheckBoxOutlined/>}</td>
                </tr>
                </tbody>
            </React.Fragment>

        );
    }
}

export default ExaminationResult;
