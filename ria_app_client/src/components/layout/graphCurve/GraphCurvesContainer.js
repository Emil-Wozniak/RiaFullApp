import React, { Component } from "react";
import { Card, CardBody, Table, Row, Col, Collapse, Button } from "reactstrap";
import Paper from "@material-ui/core/Paper";
import StandardPoint from "./StandardPoint";
import CurveParameters from "./CurveParameters";
import GraphCurveLines from "./GraphCurveLines";

class GraphCurvesContainer extends Component {
  constructor(props) {
    super(props);
    this.toggle = this.toggle.bind(this);
    this.state = { collapse: true };
  }

  toggle() {
    this.setState(state => ({ collapse: !state.collapse }));
  }

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

    var correlation;
    let slope;
    let graph_coordinates = [];
    for (let i = 0; i < graph_curves_prop.length; i++) {
      graph_coordinates.push(graph_curves_prop[i]);
    }

    let parameters = graph_curves_prop.map(parameters => (
      <CurveParameters parameters={parameters} />
    ));

    let standardPoint = graph_coordinates[0].graphCurveLines
      .sort((a, b) => a.id > b.id)
      .map((standardPoint, i) => (
        <StandardPoint key={i} standardPoint={standardPoint} />
      ));

    var moreThan10 = ">10%";
    var between5and10 = "5-10%";
    var equalStandard = "almost equals";

    slope = graph_coordinates[0].regressionParameterB;
    correlation = graph_coordinates[0].correlation;

    const isGraphPresent = graph_curves_prop => {
      if (correlation === null || slope === null) {
        return (
          <div className="alert alert-danger text-center" role="alert">
            No curve
          </div>
        );
      } else {
        return (
          <React.Fragment>
            <Row>
              <Col md="9" style={{ paddingRight: 4 }}>
                <Card>
              
                  <CardBody style={{ padding: 1, margin: 1 }}>
                    <GraphCurveLines graph_curves_prop={graph_curves_prop} />
                  </CardBody>
                  <Row>
                    <Col md="7" style={{ paddingRight: 4 }}>
                      <div className="map-legend">
                        {parameters}
                        <Row className="parameter-row">
                          <p className="map-legend-tittle">Correlation:</p>
                          <p className="map-legend-value">
                            {graph_curves_prop[0].correlation}
                          </p>
                        </Row>
                        <Row className="parameter-row">
                          <p className="map-legend-tittle">%:</p>
                          <p className="map-legend-value">
                            {graph_curves_prop[0].zeroBindingPercent}
                          </p>
                        </Row>
                        <Row className="parameter-row">
                          <p className="map-legend-tittle">Slope:</p>
                          <p className="map-legend-value">
                            {graph_curves_prop[0].regressionParameterB}
                          </p>
                        </Row>
                      </div>
                    </Col>
                    <Col md="5" style={{ paddingLeft: 4 }}>
                      <div className="map-legend">
                        <p className="legend">
                          <div className="legend-color legend-color-red" />
                          {moreThan10}
                        </p>
                        <p className="legend">
                          <div className="legend-color legend-color-blue" />
                          {between5and10}
                        </p>
                        <p className="legend">
                          <div className="legend-color legend-color-green" />
                          {equalStandard}
                        </p>
                      </div>
                    </Col>
                  </Row>
                </Card>
              </Col>
              <Col md="3" style={{ paddingLeft: 4 }}>
                <Card>
                  <Button className="btn-sm legend-btn" onClick={this.toggle}>
                    Standard table:
                  </Button>
                  <Collapse isOpen={this.state.collapse}>
                    <CardBody style={{ padding: 0, margin: 0 }}>
                      <Table className="standard-table table-sm" striped>
                        <thead>
                          <tr>
                            <th width="60">
                              <p style={{ fontWeight: "bold" }}>
                                Standard exp.:
                              </p>
                            </th>
                            <th width="60">
                              <p style={{ fontWeight: "bold" }}>
                                Standard read:
                              </p>
                            </th>
                          </tr>
                        </thead>
                        {standardPoint}
                      </Table>
                    </CardBody>
                  </Collapse>
                </Card>
              </Col>
            </Row>
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
