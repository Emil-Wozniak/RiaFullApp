import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { deleteFileEntity } from "../../../actions/filesActions";
import { withStyles } from "@material-ui/core/styles";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  paper: {
    padding: theme.spacing.unit * 2,
    textAlign: "center",
    color: theme.palette.text.secondary
  }
});

class Result extends Component {
  render() {
    const { result } = this.props;

    return (
      <React.Fragment>
        <tbody>
          <tr>
            <th scope="row"></th>
            <td> {result.samples}</td>
            <td>{result.position}</td>
            <td>{result.ccpm}</td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

Result.propTypes = {
  classes: PropTypes.object.isRequired,
  result: PropTypes.object.isRequired,
  deleteFileEntity: PropTypes.func.isRequired
};

export default compose(
  withStyles(styles),
  connect(
    null,
    { deleteFileEntity }
  )
)(Result);
