import React, { Component } from "react";
import CanvasJSReact from "../../canvasjs/canvasjs.react";
import { number } from "prop-types";
import { Container, Row } from "reactstrap";
import IconButton from "@material-ui/core/IconButton";
import { Print, CloudDownload, Style } from "@material-ui/icons/";

var CanvasJSChart = CanvasJSReact.CanvasJSChart;
var startTime = 0;
var endTime = 0;

class GraphCurveLines extends Component {
  constructor() {
    super();
    this.toggleDataSeries = this.toggleDataSeries.bind(this);
    this.downloadChart = this.downloadChart.bind(this);
    this.printChart = this.printChart.bind(this);
    this.changeTheme = this.changeTheme.bind(this);
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

  downloadChart() {
    var chart = this.chart;
    chart.exportChart({ format: "png" });
  }
  printChart() {
    var chart = this.chart;
    chart.print();
  }

  changeTheme() {
    var chart = this.chart;
    var chartType = document.getElementById("chartType");
    chartType.addEventListener("change", function() {
      chart.options.theme = chartType.options[chartType.selectedIndex].value;
      chart.render();
    });
  }

  render() {
    const { graph_curves_prop } = this.props;

    var dataError = [];
    var dataPoints = [];
    let varX = [];
    let varY1 = [];
    let varY2 = [];
    let varAverage = [];

    for (let i = 0; i < graph_curves_prop.graphCurveLines.length; i++) {
      graph_curves_prop.graphCurveLines.sortAttr("id");
      if (graph_curves_prop.graphCurveLines[i].id % 2 === 0) {
        varX.push(graph_curves_prop.graphCurveLines[i].x);
        varY1.push(graph_curves_prop.graphCurveLines[i].y);
      }
      if (!(graph_curves_prop.graphCurveLines[i].id % 2 === 0)) {
        varY2.push(graph_curves_prop.graphCurveLines[i].y);
      }
    }

    for (let i = 0; i < varY1.length; i++) {
      let middle = varY2[i] + varY1[i];
      middle = middle / 2;
      varAverage.push(middle);
    }

    for (let i = 0; i < varX.length; i++) {
      dataPoints.push({ x: varX[i], y: varAverage[i] });
      dataError.push({ x: varX[i], y: [varY1[i], varY2[i]] });
    }

    const options = {
      theme: "light2",
      zoomEnabled: true,
      animationEnabled: true,
      title: {
        text: "Calibration curve:"
      },
      legend: {
        fontFamily: "SF mono",
        fontweight: "bold"
      },
      axisX: {
        title: "Logarithm (ng/ml)",
        fontFamily: "Helvetica, Arial, Sans-Serif",
        interval: varX,
        gridDashType: "dot",
        interlacedColor: "#e5ffff",
        gridThickness: 2,
        gridColor: "#ccff90",
        crosshair: {
          enabled: true,
          snapToDataPoint: true,
          labelFontFamily: "SF mono",
          labelFontColor: "#e5ffff"
        }
      },
      axisY: {
        interval: 0.2,
        title: "Logarithm B",
        fontFamily: "Helvetica, Arial, Sans-Serif",
        includeZero: true,
        intervalType: number,
        lineColor: "blue",
        lineDashType: "dash",
        crosshair: {
          enabled: true,
          snapToDataPoint: true,
          labelFontFamily: "SF mono",
          labelFontColor: "#e5ffff"
        }
      },
      data: [
        {
          type: "line",
          name: "Average",
          showInLegend: true,
          toolTipContent:
            '<b>{label}</b><br><span style="color:#4F81BC">{name}</span>: {y} pg',
          dataPoints: dataPoints
        },
        {
          type: "error",
          name: "Error Range",
          showInLegend: true,
          toolTipContent:
            '<span style="color:#C0504E">{name}</span>: {y[0]} - {y[1]} pg',
          dataPoints: dataError
        }
      ]
    };

    startTime = new Date();

    return (
      <div>
        <Container>
          <Row>
            <IconButton className="btn-sm" onClick={this.downloadChart}>
              <CloudDownload />
            </IconButton>
            <IconButton
              className="btn-sm"
              onClick={this.printChart}
              style={{ paddingLeft: 10 }}
            >
              <Print />
            </IconButton>
            <div type="button" className=" btn btn-group dropright btn-xs">
              <select type="button"
                className="btn btn-secondary dropdown-toggle btn-xs"
                onClick={this.changeTheme}
                id="chartType"
                name="Chart Type"
              >
                <option className="theme-btn" value="light1">
                  Light 1
                </option>
                <option className="theme-btn" value="light1" value="light2">
                  Light 2
                </option>
                <option className="theme-btn" value="light1" value="dark1">
                  Dark 1
                </option>
                <option className="theme-btn" value="light1" value="dark2">
                  Dark 2
                </option>
              </select>
            </div>
          </Row>
        </Container>
        <CanvasJSChart options={options} onRef={ref => (this.chart = ref)} />
      </div>
    );
  }

  componentDidMount() {
    var chart = this.chart;
    chart.render();
  }
}

export default GraphCurveLines;
