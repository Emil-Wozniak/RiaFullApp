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
    let varY1 = [];
    let varY2 = [];
    let varAverage = [];

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
        varY1.push(graph_curves_prop[i].y);
      }
    }
    for (let i = 0; i < graph_curves_prop.length; i++) {
      graph_curves_prop.sortAttr("id");
      if (!(graph_curves_prop[i].id % 2)) {
        varY2.push(graph_curves_prop[i].y);
      }
    }
    for (let i = 0; i < varY1.length; i++) {
      let middle = varY2[i] + varY1[i];
      middle = middle / 2;
        varAverage.push(middle);
      
    }

    let varControl = [];
    if (graph_curves_prop.length > 0) {
      graph_curves_prop.sortAttr("id");
      varControl.push(graph_curves_prop[0].y);
      varControl.push(graph_curves_prop[13].y);
    }

    var lineChartData = {
      labels: varX,
      series: [varY1, varY2, {
        name: 'series-3',
        data: varAverage
      }]
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
      series:{
        'series-3':{
          showLine: true,
          showPoint: false
        }
      }
    };

    return (
      <React.Fragment>
        <div>Correlation: {r[1]}</div>
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
