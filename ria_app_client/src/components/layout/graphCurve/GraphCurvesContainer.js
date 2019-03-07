import React, { Component } from "react";
import GraphCurves from "./GraphCurves";
import ChartistGraph from "react-chartist";

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

    const graph_curves = graph_curves_prop
      .sort((a, b) => a.id > b.id)
      .map((graph_curve, i) => (
        <GraphCurves key={i} graph_curve={graph_curve} />
      ));

    let varX = [];
    let varY = [];
    for (let i = 0; i < graph_curves_prop.length; i++) {
      graph_curves_prop.sortAttr("id");
      if (graph_curves_prop[i].id % 2) {
        varX.push(graph_curves_prop[i].x);
      }
    }

    for (let i = 0; i < graph_curves_prop.length; i++) {
      graph_curves_prop.sortAttr("id");
      if (graph_curves_prop[i].id%2) {
        varY.push(graph_curves_prop[i].y);
      }
    }
    let varControl = [];
    if (graph_curves_prop.length >0) {
      graph_curves_prop.sortAttr("id");
        varControl.push(graph_curves_prop[0].y);
        varControl.push(graph_curves_prop[13].y)
    }

    var lineChartData = {
      labels: varX,
      series: [varY]
    };

    var lineChartOptions = {
      high: 2,
      low: -2,
      fullWidth: true,
      showPoint: true,
      showGrid: true,
      showArea: false,
      showLabel: false,
 
      axisX: {
        showGrid: true,
        showLabel: true,
     
      },
      axisY: {
        showGrid: true,
        showLabel: true,
      },
      chartPadding: 0
    };
    return (
      <React.Fragment>
        Var control: {varControl +" , "}
        <br/>
        var Y: {varY + " , "}
          <br />
          <ChartistGraph
            data={lineChartData}
            options={lineChartOptions}
            type={"Line"}
          />
      </React.Fragment>
    );
  }
}
export default GraphCurvesContainer;
