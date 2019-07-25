import React, {Component} from 'react';
import Col from "reactstrap/es/Col";
import CanvasJSReact from "../../canvasjs/canvasjs.react";
import {number} from "prop-types";
import {Container, Row} from "reactstrap";

let CanvasJSChart = CanvasJSReact.CanvasJSChart;
const startTime = 0;
const endTime = 10;

class GraphLine extends Component {

    render() {
        const {graph_lines} = this.props;
        const x_axis = () => graph_lines.map(line => (
            <Col>{line.x}</Col>
        ));

        const y_axis = () => graph_lines.map(line => (
            <Col>{line.y}</Col>
        ));

        var dataError = [];
        var dataPoints = [];
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

        function getAxis(varAverage) {
            let first = 0;
            let second = 0;
            let third = 0;
            let middle = 0;
            let fifth = 0;
            let sixth = 0;
            let last = 0;

            for (let index = 0; index < varAverage.length; index++) {
                first = varAverage[0];
                last = varAverage[6];
            }
            var outside = 67.5;
            var inside = 40.5;
            middle = (first + last) / 2;

            second = (first / 100) * outside;
            third = (first / 100) * inside;

            fifth = (last / 100) * inside;
            sixth = (last / 100) * outside;

            slopeArray.push(first);
            slopeArray.push(second);
            slopeArray.push(third);
            slopeArray.push(middle);
            slopeArray.push(fifth);
            slopeArray.push(sixth);
            slopeArray.push(last);
        }

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
                text: "Calibration curve:"
            },
            toolTip: {
                shared: true
            },
            legend: {
                fontFamily: "SF mono",
                fontweight: "bold"
            },
            axisX: {
                title: "Logarithm (ng/ml)",
                fontFamily: "Helvetica, Arial, Sans-Serif",
                fontSize:10,
                interval: x_axis(),
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
