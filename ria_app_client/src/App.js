import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import "./App.css";
import store from "./store";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { Provider } from "react-redux";
import { withStyles } from "@material-ui/core/styles";
import FileBoard from "./components/FileBoard";
import Dashboard from "./components/Dashboard";
import Navbar from "./components/layout/ui/Navbar";
import Landing from "./components/layout/Landing"

const styles = theme => ({
  root: {
    flexGrow: 1,
    overflow: "hidden",
    padding: `0 ${theme.spacing.unit * 3}px`
  },
  paper: {
    maxWidth: 400,
    margin: `${theme.spacing.unit}px auto`,
    padding: theme.spacing.unit * 2
  }
});

class App extends Component {
  state = {
    direction: "row",
    justify: "center",
    alignItems: "center"
  };

  handleChange = key => (event, value) => {
    this.setState({
      [key]: value
    });
  };

  render() {
    return (
      <Provider store={store}>
        <Router>
          <div className="App">
            <Navbar />
            <Route exact path="/" component={Landing} />
            <Route exact path="/dashboard" component={Dashboard} />
            <Route exact path="/fileBoard/:dataId" component={FileBoard} />
          </div>
        </Router>
      </Provider>
    );
  }
}

App.propTypes = {
  classes: PropTypes.object.isRequired
};

export default compose(withStyles(styles))(App);
