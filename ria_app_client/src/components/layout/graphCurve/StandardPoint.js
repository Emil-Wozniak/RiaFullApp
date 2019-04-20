import React, { Component } from "react";

class StandardPoint extends Component {
  render() {
    const { graphCurveLines} = this.props;
    let spreadCondition = 0;
    var spreadStyle;
    console.log(spreadStyle);
    var standardPoint =[];

   for (let i = 0; i <graphCurveLines.length; i++) {
     const element = graphCurveLines[i];
     standardPoint.push(element);
   }

    function getSpreadLevel({standardPoint}) {
      if (
        (graphCurveLines.meterReading * 100) / graphCurveLines.standard >
        110
      ) {
        return (spreadCondition = 1);
      } else if (
        (graphCurveLines.meterReading * 100) / graphCurveLines.standard >=
        105
      ) {
        return (spreadCondition = 0);
      } else {
        return (spreadCondition = -1);
      }
    }

    function getSpreadValue() {
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
            <td className="mx-auto" style={getSpreadValue()}>
              {graphCurveLines.standard}
            </td>
            <td className="mx-auto" style={getSpreadValue()}>
              {graphCurveLines.meterReading}
            </td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

export default StandardPoint;
