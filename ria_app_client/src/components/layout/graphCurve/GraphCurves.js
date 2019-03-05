import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";
import ChartistGraph from "react-chartist";

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
    var data = {
      labels: [graph_curve.x],
      series: [[graph_curve.y]]
    };
    var options = {
      high: 10,
      low: -10,
      axisX: {
        labelInterpolationFnc: function(value, index) {
          return index % 2 === 0 ? value : null;
        }
      }
    };
    var type = "Bar";
    return (
      <React.Fragment>
        <ChartistGraph data={data} options={options} type={type} />
      </React.Fragment>
    );
  }
}

GraphCurves.propTypes = {
  classes: PropTypes.object.isRequired,
  graph_curve: PropTypes.object.isRequired
};

export default compose(withStyles(styles))(GraphCurves);
