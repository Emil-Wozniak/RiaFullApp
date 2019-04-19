import React, { Component } from "react";
import CanvasJSReact from "../../canvasjs/canvasjs.react";
var CanvasJS = CanvasJSReact.CanvasJS;
var CanvasJSChart = CanvasJSReact.CanvasJSChart;
var startTime = 0,
  endTime = 0;

class GraphCurveLines extends Component {
  constructor() {
    super();
    this.toggleDataSeries = this.toggleDataSeries.bind(this);
  }

  toggleDataSeries(e) {
    if (typeof e.dataSeries.visible === "undefined" || e.dataSeries.visible) {
      e.dataSeries.visible = false;
    } else {
      e.dataSeries.visible = true;
    }
    this.chart.render();
  }
  componentDidMount() {
    endTime = new Date();
  }

  render() {
    const { graph_curves_prop } = this.props;

    var dataError = [];
    var dataSeries = { type: "line" };
    var dataPoints = [];

    var graph_coordinates = [];
    let varX = [];
    let varY1 = [];
    let varY2 = [];
    let varAverage = [];

    for (let i = 0; i < graph_curves_prop.length; i++) {
      graph_coordinates.push(graph_curves_prop[i]);
    }

    for (let i = 0; i < graph_coordinates[0].graphCurveLines.length; i++) {
      graph_coordinates.sortAttr("id");
      if (graph_curves_prop[0].graphCurveLines[i].id % 2 === 0) {
        varX.push(graph_coordinates[0].graphCurveLines[i].x);
        varY1.push(graph_coordinates[0].graphCurveLines[i].y);
      }
      if (!(graph_coordinates[0].graphCurveLines[i].id % 2 === 0)) {
        varY2.push(graph_coordinates[0].graphCurveLines[i].y);
      }
    }

    for (let i = 0; i < varY1.length; i++) {
      let middle = varY2[i] + varY1[i];
      middle = middle / 2;
      varAverage.push(middle);
    }

    for (let i = 0; i < varX.length; i++) {
      dataPoints.push({ x: varX[i], y:varAverage[i] });
      dataError.push({x: varX[i], y:[varY1[i], varY2[i]]})
    }

    const spanStyle = {
      position: "absolute",
      top: "10px",
      fontSize: "20px",
      fontWeight: "bold",
      backgroundColor: "#d85757",
      padding: "0px 4px",
      color: "#ffffff"
    };

    const options = {
      zoomEnabled: true,
      animationEnabled: true,
      title: {
        text: "Calibration curve:"
      },
      axisX: {
        interval:varX
      },
      axisY: {
        includeZero: false
      },
      data: [{
				type: "line",
				name: "Average",
				showInLegend: true,
				toolTipContent: "<b>{label}</b><br><span style=\"color:#4F81BC\">{name}</span>: {y} pg",
        dataPoints: dataPoints // random data
        
      },
      {
				type: "error",
				name: "Error Range",
				showInLegend: true,
				toolTipContent: "<span style=\"color:#C0504E\">{name}</span>: {y[0]} - {y[1]} pg",
        dataPoints: dataError
      }
    ]
    };

    startTime = new Date();

    return (
      <div>
        <CanvasJSChart options={options} onRef={ref => (this.chart = ref)} />
      </div>
    );
  }
}

export default GraphCurveLines;
