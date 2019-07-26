import React, {Component} from 'react';
import {Col} from "reactstrap";

class ControlCurve extends Component {
    render() {
        const {control_curve} = this.props;
        const flag_condition = control_curve.flagged === true;
        let meterRead = control_curve.meterRead;
        const fixed = meterRead !== null ? parseFloat(meterRead).toFixed(2) : "-";

        const checkSpread = () => {
            const spread = [];
            for (let i = 8; i < control_curve.position.length - 2; i++) {
                let position_point = control_curve.position[i];
                let read_point = control_curve.meterRead[i];
                if (parseFloat(position_point) - parseFloat(read_point) > parseFloat(position_point) / 10) {
                    spread.push("tak");
                }
            }
            return spread;
        };

        return (
            <React.Fragment>
                <tbody>
                <tr className={"Control-Curve-Font"}>
                    <td className={"Control-Curve-Font"}><Col>{control_curve.position}</Col></td>
                    <td className={"Control-Curve-Font"}
                        style={{backgroundColor: flag_condition ? "#f05545" : "striped"}}><Col
                        className={"Control-Curve-Font"}>{control_curve.cpm}</Col></td>
                    <td className={"Control-Curve-Font"}><Col>{fixed}</Col></td>
                    {checkSpread()}
                </tr>
                </tbody>
            </React.Fragment>
        );
    }
}

export default ControlCurve;
