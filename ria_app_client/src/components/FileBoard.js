import React, { Component } from "react";
import Backlog from "./layout/backlog/Backlog";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { getBacklog } from "../actions/backlogActions";

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
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.errors) {
      this.setState({ errors: nextProps.errors });
    }
  }

  render() {
    const { dataId } = this.props.match.params;
    const { results } = this.props.backlog;
    const { errors } = this.state;

    let BoardContent;

    const boardAlgorithm = (errors, results) => {
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
        return <Backlog results_prop={results} />;
      }
    };

    BoardContent = boardAlgorithm(errors, results);

    return (
      <div className="container">
      <hr/>
        {BoardContent}
     
      </div>
    );
  }
}

FileBoard.propTypes = {
  backlog: PropTypes.object.isRequired,
  getBacklog: PropTypes.func.isRequired,
  errors: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
  backlog: state.backlog,
  errors: state.errors
});

export default connect(
  mapStateToProps,
  { getBacklog }
)(FileBoard);
