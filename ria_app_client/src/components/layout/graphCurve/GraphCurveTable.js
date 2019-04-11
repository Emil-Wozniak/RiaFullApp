import React, { Component } from "react";
import { Table } from "reactstrap";

class GraphCurveTable extends Component {
  render() {
    const { graph_curves_prop } = this.props;
    var r = [];

    let varBinding = [];
    if (graph_curves_prop.length > 0) {
      graph_curves_prop.sortAttr("id");
      r.push(graph_curves_prop[0].r);
    }
    if (graph_curves_prop.length > 0) {
      graph_curves_prop.sortAttr("id");
      varBinding.push(graph_curves_prop[0].zeroBindingPercent);
    }

    return (
      <React.Fragment>
        <Table>
          <thead>
            <tr>
              <th>Correlation</th>
              <th>%:</th>
            </tr>
          </thead>
          <tbody>
            <tr>
              <td>{r[1]}</td>
              <td> {varBinding[1]}</td>
            </tr>
          </tbody>
        </Table>
      </React.Fragment>
    );
  }
}
export default GraphCurveTable;
