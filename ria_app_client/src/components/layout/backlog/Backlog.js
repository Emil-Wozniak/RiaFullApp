import React, { Component } from "react";
import { Table } from "reactstrap";
import Result from "../result/Result";

class Backlog extends Component {
  constructor(props) {
    super(props);
    this.state = { results: [] };
  }

  render() {
    const { results_prop } = this.props;

    const results = results_prop.sort((a, b) => a.samples > b.samples).map((result, i) => 
      <Result key={i} result={result} />
    );

    return (
      <React.Fragment>
        <Table
        striped>
          <thead>
            <tr>
              <th>sample:</th>
              <th>position</th>
              <th>CPM:</th>
              <th>ng</th>
            </tr>
          </thead>
          {results}
          
        </Table>
      </React.Fragment>
    );
  }
}

export default Backlog;
