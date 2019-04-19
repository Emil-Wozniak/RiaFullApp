import React, { Component } from "react";

class StandardPoint extends Component {
  render() {
    const { standardPoint } = this.props;
    let spreadCondition = 0;
    var spreadStyle;
    console.log(spreadStyle)

    function getSpreadLevel(standardPoint) {
      if (
        Math.abs((standardPoint.meterReading * 100) / standardPoint.standard) >
        110
      ) {
        return (spreadCondition = 1);
      } else if (
        Math.abs((standardPoint.meterReading * 100) / standardPoint.standard) >=
        105
      ) {
        return (spreadCondition = 0);
      } else {
        return (spreadCondition = -1);
      }
    }

    function getSpreadValue(standardPoint) {
      getSpreadLevel(standardPoint);
      if (spreadCondition === 1) {
        return (spreadStyle = {
          backgroundColor: "#ff7961"
        });
      } else if (spreadCondition === 0) {
        return (spreadStyle = {
          backgroundColor: "#e5ffff"
        });
      } else {
        return (spreadStyle = {
          backgroundColor: "#CCFF90"
        });
      }
    }

    return (
      <React.Fragment>
        <tbody>
          <tr>
            <td className="mx-auto" style={getSpreadValue(standardPoint)}>
              {standardPoint.standard}
            </td>
            <td className="mx-auto" style={getSpreadValue(standardPoint)}>
              {standardPoint.meterReading}
            </td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

export default StandardPoint;
