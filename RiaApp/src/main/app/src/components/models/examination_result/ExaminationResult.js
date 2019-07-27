import React, {Component} from 'react';
import Col from "reactstrap/es/Col";

class ExaminationResult extends Component {
    render() {
        const {probeNumber, position, cpm, flagged, ng, id, identifier, pattern, average} = this.props.examination_point;
        const filename = probeNumber === 25 ? <Col className={"cell-font-format"}>{identifier}</Col> :
            probeNumber === 26 ? <Col className={"cell-font-format"}>{pattern}</Col> : <Col/>;

        return (
            <React.Fragment>
                <tbody>
                <tr>
                    <td className={"Examination-Result-Average"}>{position}</td>
                    <td><Col className={"cell-font-format"}>{probeNumber}</Col></td>
                    <td><Col className={"cell-font-format"}>{cpm}</Col></td>
                    <td style={{backgroundColor: flagged ? "#f05545" : "striped"}}>
                        <Col className={"cell-font-format"}>{ng}</Col>
                    </td>
                    <td><Col className={"cell-font-format"}>{average}</Col></td>
                    <td>{filename}</td>
                </tr>
                </tbody>
            </React.Fragment>

        );
    }
}

export default ExaminationResult;
