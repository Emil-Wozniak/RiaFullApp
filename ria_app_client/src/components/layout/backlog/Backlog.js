import React, { Component } from "react";
import Result from "../result/Result";

class Backlog extends Component {
  render() {
    const { results_prop } = this.props;

    const results = results_prop.map(result => (
      <Result key={result.id} result={result} />
    ));

    let todoItems = [];
    let inProgressItems = [];
    let doneItems = [];

    for (let i = 0; i < results.length; i++) {
      if (results[i].props.result.status === "TO_DO") {
        todoItems.push(results[i]);
      }

      if (results[i].props.result.status === "IN_PROGRESS") {
        inProgressItems.push(results[i]);
      }

      if (results[i].props.result.status === "DONE") {
        doneItems.push(results[i]);
      }
    }

    return (
      <div>
      <Result/>
          <div className="container">
            <div className="row">
              <div className="col-md-4">
                <div className="card text-center mb-2">
                  <div className="card-header bg-secondary text-white">
                    <h3>TO DO</h3>
                  </div>
                </div>
                {todoItems}
              </div>
              <div className="col-md-4">
                <div className="card text-center mb-2">
                  <div className="card-header bg-primary text-white">
                    <h3>In Progress</h3>
                  </div>
                </div>
                {inProgressItems}
              </div>
              <div className="col-md-4">
                <div className="card text-center mb-2">
                  <div className="card-header bg-success text-white">
                    <h3>Done</h3>
                  </div>
                </div>
                {doneItems}
              </div>
            </div>
          </div>
      </div>
    );
  }
}

export default Backlog;
