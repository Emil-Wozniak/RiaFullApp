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

class ControlCurve extends Component {
  render() {
    const { control_curve } = this.props;

    return (
      <React.Fragment>
        <tbody>
          <tr>
            <td>{control_curve.position}</td>
            <td>{control_curve.ccpm}</td>
            <td/>
            <td />
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

ControlCurve.propTypes = {
  classes: PropTypes.object.isRequired,
  control_curve: PropTypes.object.isRequired,
  deleteFileEntity: PropTypes.func.isRequired
};

export default compose(
  withStyles(styles),
  connect(
    null,
    { deleteFileEntity }
  )
)(ControlCurve);
