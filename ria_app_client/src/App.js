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
import Landing from "./components/layout/Landing";
import Register from "./components/layout/user/Register";
import Login from "./components/layout/user/Login";
import setJwtToken from "./components/layout/security/SetJwtToken";
import jwt_decode from "jwt-decode";
import { SET_CURRENT_USER } from "./actions/types";
import { logout } from "./actions/securityActions";

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

const jwtToken = localStorage.jwtToken;

if (jwtToken) {
  setJwtToken(jwtToken);
  const decode_jwtToken = jwt_decode(jwtToken);
  store.dispatch({
    type: SET_CURRENT_USER,
    payload: decode_jwtToken
  });

  const currentTime = Date.now() / 1000;
  if (decode_jwtToken.exp < currentTime) {
    //handel logout
    store.dispatch(logout());
    window.location.href = "/";
  }
}

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
            {
              //Public Routes
            }
            <Route exact path="/register" component={Register} />
            <Route exact path="/login" component={Login} />
            <Route exact path="/" component={Landing} />
            {
              //Private Routes
            }
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
