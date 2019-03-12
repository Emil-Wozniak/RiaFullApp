import React, { Component } from "react";
import ChartistGraph from "react-chartist";
import Chartist from "chartist";

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

    var r = [];
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
      if (graph_curves_prop[i].id % 2) {
        r.push(graph_curves_prop[i].r);
      }
    }

    for (let i = 0; i < graph_curves_prop.length; i++) {
      graph_curves_prop.sortAttr("id");
      if (graph_curves_prop[i].id % 2) {
        varY.push(graph_curves_prop[i].y);
      }
    }
    
    let varControl = [];
    if (graph_curves_prop.length > 0) {
      graph_curves_prop.sortAttr("id");
      varControl.push(graph_curves_prop[0].y);
      varControl.push(graph_curves_prop[13].y);
    }

    var lineChartData = {
      labels: varX,
      series: [varY]
    };

    var lineChartOptions = {
      type:"Bar",
      high: 2,
      low: -2,
      fullWidth: false,
      showPoint: true,
      showGrid: true,
      showArea: true,

      axisX: {
        showGrid: true,
        showLabel: true
      },
      axisY: {
        showGrid: true,
        showLabel: true
      },
      chartPadding: {
        right: 50,
        left: 0,
        top: 20
      },
      lineSmooth: Chartist.Interpolation.cardinal({
        fillHoles: true,
      }),
    };


    return (
      <React.Fragment>
      <div>R: {r[1]}</div>
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
