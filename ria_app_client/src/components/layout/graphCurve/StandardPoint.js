import React, { Component } from "react";

class StandardPoint extends Component {
  render() {
    const { standardPoint } = this.props;

    function isStandardSpreadCorrect(standardPoint) {
      if (
        Math.abs( (standardPoint.meterReading*100)/standardPoint.standard) > 110
      ) {
        return 1;
      } else {
        return 0;
      }
    }
    const spreadCondition = isStandardSpreadCorrect(standardPoint) === 0;
    let spreadStyle = {
      backgroundColor: spreadCondition ? "#e5ffff" : "#f05545"
    };
    return (
      <React.Fragment>
        <tbody>
          <tr>
            <td className="mx-auto" style={spreadStyle}>
              {standardPoint.standard}
            </td>
            <td className="mx-auto" style={spreadStyle}>
              {standardPoint.meterReading}
            </td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

export default StandardPoint;
