import React, { Component } from "react";
import { Table } from "reactstrap";
import Result from "../result/Result";

class Backlog extends Component {
  render() {
    const { results_prop } = this.props;

    const results = results_prop.map(result => (
      <Result key={result.id} result={result} />
    ));

    return (
      <div className="container">
        <hr />
        <Table striped>
          <thead>
            <tr>
              <th>#</th>
              <th>samples:</th>
              <th>position</th>
              <th>CCMP:</th>
              <th />
            </tr>
          </thead>
          {results}
        </Table>
      </div>
    );
  }
}

export default Backlog;
