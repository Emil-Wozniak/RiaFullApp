import React, {Component} from 'react';
import CanvasJSReact from "../../canvasjs/canvasjs.react";
import {number} from "prop-types";

let CanvasJSChart = CanvasJSReact.CanvasJSChart;

class GraphLine extends Component {

    render() {
        const {graph_lines} = this.props;
        const {graph} = this.props;

        const dataError = [];
        const dataPoints = [];
        let slopeLine = [];
        let x_line = [];
        let varY1 = [];
        let varY2 = [];
        let varAverage = [];
        let slope_line = [];

        for (let i = 0; i < graph_lines.length; i++) {
            if (graph_lines[i].id % 2 === 0) {
                x_line.push(graph_lines[i].x);
                varY1.push(graph_lines[i].y);
            }
            if (!(graph_lines[i].id % 2 === 0)) {
                varY2.push(graph_lines[i].y);
            }
        }

        for (let i = 0; i < varY1.length; i++) {
            let middle = varY2[i] + varY1[i];
            middle = middle / 2;
            middle.toPrecision(2);
            varAverage.push(middle);
        }

        let correlation, regressionA, regressionB;
        const getAxis = function () {
            let x_slope = [];
            for (let i = 0; i < graph_lines.length; i++) {
                x_slope.push(graph_lines[i].x);
            }
            for (let i = 0; i < graph.length; i++) {
                correlation = graph[i].correlation;
                regressionA = graph[i].regressionParameterA;
                regressionB = graph[i].regressionParameterB;
            }
            for (let i = 0; i < x_slope.length; i += 2) {
                let y = parseFloat(x_slope[i]);
                let point = (parseFloat(regressionB)) * y + parseFloat(regressionA);
                slope_line.push(point);
            }
        };

        getAxis();
        for (let i = 0; i < x_line.length; i++) {
            dataPoints.push({x: x_line[i], y: varAverage[i]});
            dataError.push({x: x_line[i], y: [varY1[i], varY2[i]]});
            slopeLine.push({x: x_line[i], y: slope_line[i]});
        }

        const options = {
            theme: "light2",
            zoomEnabled: true,
            animationEnabled: true,
            title: {
                text: "Calibration curve:",
                fontSize: 12
            },
            toolTip: {
                shared: true
            },
            legend: {
                fontFamily: "SF mono",
                fontSize: 10,
                fontWeight: "bold"
            },
            axisX: {
                title: "Logarithm (ng/ml)",
                fontFamily: "Helvetica, Arial, Sans-Serif",
                interval: x_line,
                gridDashType: "dot",
                interlacedColor: "#e5ffff",
                gridThickness: 2,
                gridColor: "#ccff90",
                crosshair: {
                    enabled: true,
                    snapToDataPoint: true,
                    labelFontFamily: "SF mono",
                    labelFontColor: "#e5ffff",
                    fontSize: 10
                }
            },
            axisY: {
                interval: 0.2,
                title: "Logarithm B",
                fontFamily: "Helvetica, Arial, Sans-Serif",
                fontSize: 10,
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
                    markerType: "circle",
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
                },
                {
                    type: "line",
                    name: "Slope",
                    markerType: "none",
                    showInLegend: true,
                    toolTipContent:
                        '<b>{label}</b><br><span style="color:#4F81BC">{name}</span>: {y} pg',
                    dataPoints: slopeLine
                }
            ]
        };
        return (
            <React.Fragment>
                <CanvasJSChart options={options} onRef={ref => (this.chart = ref)}/>
            </React.Fragment>
        );
    }
}

export default GraphLine;
