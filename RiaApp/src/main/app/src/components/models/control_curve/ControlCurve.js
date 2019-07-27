import React, {Component} from 'react';

class ControlCurve extends Component {
    render() {
        const {position, cpm, flagged, meterRead} = this.props.control_curve;
        const read = meterRead !== null ? parseFloat(meterRead) : "-";
        const position_style = position === "Total" || position === "O" || position === "2.5" || position === "10.0" || position === "40.0" || position === "K" ?
            {backgroundColor: "#cfd8dc"} : {backgroundColor: "#fff"};

        return (
            <React.Fragment>
                <tbody>
                <tr style={position_style} className={"control-curve-font"}>
                    <td className={"control-curve-font"}>{position}</td>
                    <td className={"control-curve-font"}
                        style={{backgroundColor: flagged ? "#f05545" : "striped"}}>{cpm}</td>
                    <td className={"control-curve-font"}>{read}</td>
                </tr>
                </tbody>
            </React.Fragment>
        );
    }
}

export default ControlCurve;
