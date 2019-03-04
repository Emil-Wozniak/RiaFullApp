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

class GraphCurves extends Component {
  render() {
    const { graph_curve } = this.props;

    return (
      <React.Fragment>
        <tbody>
          <tr>
            
            <td
              className="mx-auto"
            >
   
            </td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

GraphCurves.propTypes = {
  classes: PropTypes.object.isRequired,
  graph_curve: PropTypes.object.isRequired,
  deleteFileEntity: PropTypes.func.isRequired
};

export default compose(
  withStyles(styles),
  connect(
    null,
    { deleteFileEntity }
  )
)(GraphCurves);
