import React, { Component } from "react";
import "./App.css";
import "bootstrap/dist/css/bootstrap.min.css";
import store from "./store";
import { Row, Container } from "reactstrap";
import { BrowserRouter as Router, Route } from "react-router-dom";
import { Provider } from "react-redux";
import Dashboard from "./components/Dashboard";
import Header from "./components/layout/Header";
import UploadFile from "./components/Upload/UploadFile";

class App extends Component {
  render() {
    return (
      <Provider store={store}>
        <Router>
          <div className="App">
            <Route exact path="/dashboard" component={Dashboard} />
            <Header />
            <Container>
              <Row>
                <UploadFile />
              </Row>
              <Dashboard />
            </Container>
          </div>
        </Router>
      </Provider>
    );
  }
}

export default App;
