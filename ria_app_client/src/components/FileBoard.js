import React, { Component } from "react";
import Backlog from "./layout/backlog/Backlog";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { getBacklog, getBacklogWithCC } from "../actions/backlogActions";
import { getGraphCurve } from "../actions/graphCurveActions";
import GraphCurvesContainer from "./layout/graphCurve/GraphCurvesContainer";

class FileBoard extends Component {
  //constructor to handle errors
  constructor() {
    super();
    this.state = {
      errors: {}
    };
  }

  componentDidMount() {
    const { dataId } = this.props.match.params;
    this.props.getBacklog(dataId);
    this.props.getBacklogWithCC(dataId);
    this.props.getGraphCurve(dataId);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.errors) {
      this.setState({ errors: nextProps.errors });
    }
  }

  render() {
    const { results, control_curves } = this.props.backlog;
    const { graph_curves } = this.props.graph_curve;
    const { errors } = this.state;

    let BoardContent;

    const boardAlgorithm = (errors, results, control_curves, graph_curves) => {
      if (results.length < 1) {
        if (errors.fileNotFound) {
          return (
            <div className="alert alert-danger text-center" role="alert">
              {errors.fileNotFound}
            </div>
          );
        } else {
          return (
            <div className="alert alert-info text-center" role="alert">
              No results on this board
            </div>
          );
        }
      } else {
        return (
          <React.Fragment>
            <GraphCurvesContainer graph_curves_prop={graph_curves} />
            <Backlog
              results_prop={results}
              control_curves_prop={control_curves}
              graph_curves_prop={graph_curves} 
            />
          </React.Fragment>
        );
      }
    };

    BoardContent = boardAlgorithm(
      errors,
      results,
      control_curves,
      graph_curves
    );

    return (
      <div className="container">
        <hr />
        {BoardContent}
      </div>
    );
  }
}

FileBoard.propTypes = {
  backlog: PropTypes.object.isRequired,
  graph_curve: PropTypes.object.isRequired,
  getBacklog: PropTypes.func.isRequired,
  getBacklogWithCC: PropTypes.func.isRequired,
  getGraphCurve: PropTypes.func.isRequired,
  errors: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
  backlog: state.backlog,
  graph_curve: state.graph_curve,
  errors: state.errors
});

export default connect(
  mapStateToProps,
  { getBacklog, getBacklogWithCC, getGraphCurve }
)(FileBoard);
