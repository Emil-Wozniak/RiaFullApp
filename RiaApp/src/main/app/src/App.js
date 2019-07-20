import React from 'react';
import compose from "recompose/compose";
import PropTypes from "prop-types";
import "./App.css";
import store from "./store";
import { BrowserRouter as Router, Route, Switch } from "react-router-dom";
import { Provider } from "react-redux";

function App() {
    const state = {
        direction: "row",
        justify: "center",
        alignItems: "center"
    };

    return (
        <Provider store={store}>
            <Router>
            <div className="App">
                <header className="App-header">
                    <p>
                        Edit <code>src/App.js</code> and save to reload.
                    </p>
                    <a className="App-link"
                       href="https://reactjs.org"
                       target="_blank"
                       rel="noopener noreferrer">
                        Learn React
                    </a>
                </header>
            </div>
            </Router>
        </Provider>
    );
}

App.propTypes = {
    classes: PropTypes.object.isRequired
};

export default App;
