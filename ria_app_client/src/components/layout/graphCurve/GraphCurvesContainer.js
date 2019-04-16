import React, { Component } from "react";
import ChartistGraph from "react-chartist";
import Chartist from "chartist";
import { Table } from "reactstrap";
import Paper from "@material-ui/core/Paper";

var thStyle = {
  fontSize: "12px",
  textAlign: "center",
  margin: "0px",
  padding: "1",
  borderRadius: "50%"
};

var tdStyle = {
  fontSize: "14px",
  margin: "1px",
  padding: "2",
  borderRadius: "10px"
};

class GraphCurvesContainer extends Component {
  render() {
    Array.prototype.sortAttr = function(attr, reverse) {
      var sorter = function(a, b) {
        var aa = a[attr];
        var bb = b[attr];
        if (aa + 0 === aa && bb + 0 === bb) return aa - bb;
        // numbers
        else return aa.localeCompare(bb); // strings
      };
      this.sort(function(a, b) {
        var result = sorter(a, b);
        if (reverse) result *= -1;
        return result;
      });
    };

    const { graph_curves_prop } = this.props;

    var correlation = [];
    var graph_coordinates = [];
    let varX = [];
    let varY1 = [];
    let varY2 = [];
    let varAverage = [];
    let slope = [];
    let standard = [];
    let standardRead = [];

    for (let i = 0; i < graph_curves_prop.length; i++) {
      slope = graph_curves_prop[i].regressionParameterB;
    }

    for (let i = 0; i < graph_curves_prop.length; i++) {
      graph_coordinates = graph_curves_prop[i].graphCurveLines;
    }

    for (let i = 0; i < graph_coordinates.length; i++) {
      graph_coordinates.sortAttr("id");
      if (graph_coordinates[i].id % 2) {
        varX.push(graph_coordinates[i].x);
      }
    }

    for (let i = 0; i < graph_coordinates.length; i++) {
      graph_coordinates.sortAttr("id");
      if (graph_coordinates[i].id % 2) {
        varY1.push(graph_coordinates[i].y);
      }
    }

    for (let i = 0; i < graph_coordinates.length; i++) {
      graph_coordinates.sortAttr("id");
      if (!(graph_coordinates[i].id % 2)) {
        varY2.push(graph_coordinates[i].y);
      }
    }

    for (let i = 0; i < varY1.length; i++) {
      let middle = varY2[i] + varY1[i];
      middle = middle / 2;
      varAverage.push(middle);
    }

    for (let i = 0; i < graph_coordinates.length; i++) {
      graph_coordinates.sortAttr("id");
      standard.push(graph_coordinates[i].standard);
    }
    for (let i = 0; i < graph_coordinates.length; i++) {
      graph_coordinates.sortAttr("id");
      standardRead.push(graph_coordinates[i].meterReading);
    }
    const standardPoint = standard.map(point => (
        <p style={tdStyle}>{point}</p>
    ));
    const standardReadPoint = standardRead.map(standardReadPoint=>
    
          <p style={tdStyle}> {standardReadPoint}</p>
    )

    let varBinding = [];
    if (graph_curves_prop.length > 0) {
      graph_curves_prop.sortAttr("id");
      correlation.push(graph_curves_prop[0].correlation);
    }
    if (graph_curves_prop.length > 0) {
      graph_curves_prop.sortAttr("id");
      varBinding.push(graph_curves_prop[0].zeroBindingPercent);
    }

    var lineChartData = {
      labels: varX,
      series: [
        varY1,
        varY2,
        {
          name: "series-3",
          data: varAverage
        }
      ]
    };

    var lineChartOptions = {
      high: 2,
      low: -2,
      showLine: false,
      fullWidth: false,
      showPoint: true,
      showGrid: true,
      showArea: false,
      chartPadding: {
        right: 30,
        left: 0,
        top: 20
      },
      lineSmooth: Chartist.Interpolation.cardinal({
        fillHoles: true
      }),
      series: {
        "series-3": {
          showLine: true,
          showPoint: false
        }
      }
    };

    const isGraphPresent = graph_curves_prop => {
      if (
        graph_curves_prop.length < 1 ||
        correlation === null ||
        varBinding === null
      ) {
        return (
          <div className="alert alert-danger text-center" role="alert">
            No curve
          </div>
        );
      } else {
        return (
          <React.Fragment>
            <ChartistGraph
              data={lineChartData}
              options={lineChartOptions}
              type={"Line"}
            />
            <Table className="table table-sm" striped>
              <thead>
                <tr>
                  <th width="60">
                    <p style={thStyle}>Correlation:</p>
                  </th>
                  <th width="60">
                    <p style={thStyle}>%:</p>
                  </th>
                  <th width="60">
                    <p style={thStyle}>\</p>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>
                    <p style={tdStyle}>{correlation}</p>
                  </td>
                  <td>
                    <p style={tdStyle}>{varBinding}</p>
                  </td>
                  <td style={tdStyle}>
                    <p>{slope}</p>
                  </td>
                </tr>
              </tbody>
            </Table>
            <Table className="table table-sm" striped>
              <thead>
                <tr>
                  <th width="60">
                    <p style={thStyle}>Standard:</p>
                  </th>
                  <th width="60">
                    <p style={thStyle}>Standard read:</p>
                  </th>
                </tr>
              </thead>
              <tbody>
                <tr>
                  <td>{standardPoint}</td>
                  <td>{standardReadPoint}</td>
                </tr>
              </tbody>
            </Table>
          </React.Fragment>
        );
      }
    };

    const GraphAlgorithm = isGraphPresent(graph_curves_prop);

    return (
      <React.Fragment>
        <Paper classes={{ paper: "paper" }}>{GraphAlgorithm}</Paper>
      </React.Fragment>
    );
  }
}
export default GraphCurvesContainer;
