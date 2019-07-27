import React, {Component} from 'react';
import CanvasJSReact from "../../canvasjs/canvasjs.react";
import {number} from "prop-types";

let CanvasJSChart = CanvasJSReact.CanvasJSChart;

class GraphLine extends Component {

    render() {
        const {graph_lines} = this.props;

        const dataError = [];
        const dataPoints = [];
        let slopeLine = [];
        let xArray = [];
        let varY1 = [];
        let varY2 = [];
        let varAverage = [];
        let slopeArray = [];

        for (let i = 0; i < graph_lines.length; i++) {

            if (graph_lines[i].id % 2 === 0) {
                xArray.push(graph_lines[i].x);
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

        const getAxis = (varAverage) => {
            let first = 0, last = 0;
            for (let index = 0; index < varAverage.length; index++) {
                first = varAverage[0];
                last = varAverage[6];
            }
            const outside = 67.5;
            const inside = 40.5;
            let middle = (first + last) / 2;
            let second = (first / 100) * outside;
            let third = (first / 100) * inside;
            let fifth = (last / 100) * inside;
            let sixth = (last / 100) * outside;

            slopeArray.push(first);
            slopeArray.push(second);
            slopeArray.push(third);
            slopeArray.push(middle);
            slopeArray.push(fifth);
            slopeArray.push(sixth);
            slopeArray.push(last);
        };

        getAxis(varAverage);

        for (let i = 0; i < xArray.length; i++) {
            dataPoints.push({x: xArray[i], y: varAverage[i]});
            dataError.push({x: xArray[i], y: [varY1[i], varY2[i]]});
            slopeLine.push({x: xArray[i], y: slopeArray[i]});
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
                interval: xArray,
                gridDashType: "dot",
                interlacedColor: "#e5ffff",
                gridThickness: 2,
                gridColor: "#ccff90",
                crosshair: {
                    enabled: true,
                    snapToDataPoint: true,
                    labelFontFamily: "SF mono",
                    labelFontColor: "#e5ffff",
                    fontSize:10
                }
            },
            axisY: {
                interval: 0.2,
                title: "Logarithm B",
                fontFamily: "Helvetica, Arial, Sans-Serif",
                fontSize:10,
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
