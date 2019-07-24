import React from 'react';
import PropTypes from "prop-types";
import "./App.css";
import store from "./store";
import {BrowserRouter as Router, Route} from "react-router-dom";
import {Provider} from "react-redux";
import Dashboard from "./components/Dashboard";

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
                    <Route exact path={"/dashboard"} component={Dashboard}/>
                </div>
            </Router>
        </Provider>
    );
}

App.propTypes = {
    classes: PropTypes.object.isRequired
};

export default App;
