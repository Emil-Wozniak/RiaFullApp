import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { deleteFileEntity } from "../../../actions/filesActions";
import { CheckBoxOutlined, CheckBoxOutlineBlank } from "@material-ui/icons";

class ControlCurve extends Component {
  render() {
    const { control_curve } = this.props;
    const condition = control_curve.flagged === false;
    const { graph_curves } = this.props;

    const isCorrelationPresent = graph_curves => {
      if (graph_curves[0].correlation === null) {
        return (
          <tbody>
          <tr>
            <td>{control_curve.position}</td>
            <td
              className="mx-auto"
              style={{ color: condition ? "green" : "red" }}
            >
              {control_curve.cpm}
            </td>
            <td>
              {condition ? <CheckBoxOutlineBlank /> : <CheckBoxOutlined />}
            </td>
          </tr>
        </tbody>
        );
      } else {
        return (
          <tbody>
          <tr>
            <td>{control_curve.position}</td>
            <td
              className="mx-auto"
              style={{ color: condition ? "green" : "red" }}
            >
              {control_curve.cpm}
            </td>
            <td>
              {condition ? <CheckBoxOutlineBlank /> : <CheckBoxOutlined />}
            </td>
            <td/>
            <td/>
          </tr>
        </tbody>
        );
      }
    };

    const present = isCorrelationPresent(graph_curves);

    return (
      <React.Fragment>
      {present}
       
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
  connect(
    null,
    { deleteFileEntity }
  )
)(ControlCurve);
