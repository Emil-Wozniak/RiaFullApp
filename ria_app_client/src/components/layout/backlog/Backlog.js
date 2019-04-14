import React, { Component } from "react";
import Result from "../result/Result";
import { withStyles } from "@material-ui/core/styles";
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles";
import createPalette from "@material-ui/core/styles/createPalette";
import * as color from "@material-ui/core/colors";
import Paper from "@material-ui/core/Paper";
import ReactToExcel from "react-html-table-to-excel";
import { Row, Container, Table } from "reactstrap";
import IconButton from "@material-ui/core/IconButton";
import { ArrowBack } from "@material-ui/icons";
import ControlCurve from "../controlCurve/ControlCurve";

var styles = theme => ({
  root: {
    flexGrow: 1
  },
  demo: {
    height: 240
  },
  paper: {
    padding: theme.spacing.unit * 2,
    height: "100%",
    color: theme.palette.text.secondary
  },
  control: {
    padding: theme.spacing.unit * 2
  }
});

const theme = (styles = createMuiTheme({
  palette: createPalette({
    type: "light",
    primary: color.purple,
    secondary: color.green,
    accent: color.grey,
    error: color.red,
    success: color.green,
    inProgress: color.yellow
  })
}));

Array.prototype.sortAttr = function(attr, reverse) {
  var sorter = function(a, b) {
    var aa = a[attr];
    var bb = b[attr];
    if (aa + 0 === aa && bb + 0 === bb) return aa - bb;
    // numbers
    else return aa.localeCompare(bb); // strings
  };
  this.sort(function(a, b) {
    var result = sorter(a, b);
    if (reverse) result *= -1;
    return result;
  });
};

class Backlog extends Component {
  render() {
    const { control_curves_prop } = this.props;
    const { results_prop } = this.props;

    control_curves_prop.sortAttr("id");
    results_prop.sortAttr("id");

    const control_curves = control_curves_prop.map(control_curve => (
      <ControlCurve key={control_curve} control_curve={control_curve} />
    ));

    const results = results_prop
      .sort((a, b) => a.samples > b.samples)
      .map((result, i) => <Result key={i} result={result} />);

    return (
      <MuiThemeProvider theme={theme}>
        <Paper classes={{ paper: "paper" }}>
          <Container>
            <Row>
              <IconButton href="/dashboard">
                <ArrowBack />
              </IconButton>
              <ReactToExcel
                className="fa fa-download fa-2x float-center"
                table="file_data"
                filename="file_data"
                sheet="sheet 1"
                buttonText=""
              />
            </Row>
          </Container>
          <Table striped id="file_data">
            <br />
            <h5>Control Curve:</h5>
            <thead>
              <tr>
                <th>position</th>
                <th>cpm</th>
                <th>flag</th>
                <th />
                <th />
              </tr>
            </thead>
            {control_curves}
            <br />
            <h5>Results:</h5>
            <thead>
              <tr>
                <th>sample</th>
                <th>&#8470;</th>
                <th>cpm</th>
                <th>&micro;g</th>
                <th>x&#772;</th>
              </tr>
            </thead>
            {results}
          </Table>
        </Paper>
      </MuiThemeProvider>
    );
  }
}

export default withStyles(styles)(Backlog);
