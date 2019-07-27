import React, {Component} from 'react';

class ExaminationResult extends Component {
    render() {
        const {probeNumber, position, cpm, flagged, ng, average} = this.props.examination_point;
        const isOdd = parseInt(position) % 2 === 1 ? {backgroundColor: "#cfd8dc"} : {backgroundColor: "#fff"};

        return (
            <React.Fragment>
                <tbody>
                <tr style={isOdd}>
                    <td className={"Examination-Result-Average"}>{position}</td>
                    <td className={"cell-font-format"}>{probeNumber}</td>
                    <td className={"cell-font-format"}>{cpm}</td>
                    <td style={{backgroundColor: flagged ? "#f05545" : "striped"}} className={"cell-font-format"}>{ng}</td>
                    <td className={"cell-font-format"}>{average}</td>
                </tr>
                </tbody>
            </React.Fragment>

        );
    }
}

export default ExaminationResult;
