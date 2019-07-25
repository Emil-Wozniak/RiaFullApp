import React, {Component} from 'react';
import {CheckBoxOutlineBlank, CheckBoxOutlined} from "@material-ui/icons";

class ExaminationResult extends Component {
    render() {
        const {examination_point} = this.props;
        const filename = (examination_point.identifier !== null) ? examination_point.identifier : "";
        const position = (examination_point.position !== null) ? examination_point.position : "";
        const probe_number = (examination_point.probeNumber !== null) ? examination_point.probeNumber : "";
        const cpm = (examination_point.cpm !== null) ? examination_point.cpm : "";
        const ng = (examination_point.ng !== null) ? examination_point.ng : "";
        const condition = examination_point.flagged === false;

        return (
            <React.Fragment>
                <tbody>
                <tr>
                    <td>{filename}</td>
                    <td>{position}</td>
                    <td>{probe_number}</td>
                    <td>{cpm}</td>
                    <td>{ng}</td>
                    <td>{condition ? <CheckBoxOutlineBlank/> : <CheckBoxOutlined/>}</td>
                </tr>
                </tbody>
            </React.Fragment>

        );
    }
}

export default ExaminationResult;
