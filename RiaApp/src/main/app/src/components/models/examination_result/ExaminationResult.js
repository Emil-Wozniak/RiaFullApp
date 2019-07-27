import React, {Component} from 'react';
import {CheckBoxOutlineBlank, CheckBoxOutlined} from "@material-ui/icons";
import Col from "reactstrap/es/Col";

class ExaminationResult extends Component {
    render() {
        const {probeNumber, position, cpm, flagged, ng, id, identifier, pattern} = this.props.examination_point;
        const ng_condition = ng === "0.0";
        const ng_value = (ng !== null) ?
            <td style={{backgroundColor: ng_condition ? "#f05545" : "striped"}}>
                <Col className={"cell-font-format"}> {ng}</Col>
            </td>
            : "";
        const condition = flagged === false;

        return (
            <React.Fragment>
                <tbody>
                <tr>
                    <td><Col className={"cell-font-format"}>{identifier}</Col></td>
                    <td className={"Examination-Result-Average"}>{position}</td>
                    <td><Col className={"cell-font-format"}>{probeNumber}</Col></td>
                    <td><Col className={"cell-font-format"}>{cpm}</Col></td>
                    {ng_value}
                    <td>{condition ? <CheckBoxOutlineBlank/> : <CheckBoxOutlined/>}</td>
                </tr>
                </tbody>
            </React.Fragment>

        );
    }
}

export default ExaminationResult;
