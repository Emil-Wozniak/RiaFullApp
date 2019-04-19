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
    const condition = result.ng === 0;
    const positionCondition = result.position % 2 === 0;

    let positionConditionStyle = {
      backgroundColor: positionCondition ? "#e5ffff" : "#fbfffc"
    };
    let isConditionTrue;
    let isOdd;
    let isCorrelationPresent;

    const position = result => {
      if (condition) {
        return (
          <td
            className="mx-auto"
            style={{ backgroundColor: condition ? "#f05545" : "striped" }}
          >
            {result.ng}
          </td>
        );
      } else {
        return (
          <td
            className="mx-auto"
            style={{
              backgroundColor: positionCondition ? "#e5ffff" : "#fbfffc"
            }}
          >
            {result.ng}
          </td>
        );
      }
    };

    const odd = result => {
      if (result.samples % 2 === 1) {
        return (
          <td className="mx-auto resultAverage" style={positionConditionStyle}>
            {result.hormoneAverage}
          </td>
        );
      } else {
        return (
          <td
            className="mx-auto resultAverage"
            style={{
              backgroundColor: positionCondition ? "#e5ffff" : "#fbfffc"
            }}
          />
        );
      }
    };
    isConditionTrue = position(result);
    isOdd = odd(result);

    const correlationPresent = result => {
      if (result.ng === null) {
        return (
          <React.Fragment>
            <tbody>
              <tr>
                <td className="mx-auto" style={positionConditionStyle}>
                  {result.samples}
                </td>
                <td className="mx-auto" style={positionConditionStyle}>
                  {result.position}
                </td>
                <td className="mx-auto" style={positionConditionStyle}>
                  {result.cpm}
                </td>
              </tr>
            </tbody>
          </React.Fragment>
        );
      } else {
        return (
          <React.Fragment>
            <tbody>
              <tr>
                <td className="mx-auto" style={positionConditionStyle}>
                  {result.samples}
                </td>
                <td
                  className="mx-auto resultNumber"
                  style={positionConditionStyle}
                >
                  {result.position}
                </td>
                <td className="mx-auto" style={positionConditionStyle}>
                  {result.cpm}
                </td>
                {isConditionTrue}
                {isOdd}
              </tr>
            </tbody>
          </React.Fragment>
        );
      }
    };
    isCorrelationPresent = correlationPresent(result);

    return <React.Fragment>{isCorrelationPresent}</React.Fragment>;
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
