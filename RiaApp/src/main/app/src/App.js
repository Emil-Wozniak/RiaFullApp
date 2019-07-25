import React from 'react';
import PropTypes from "prop-types";
import "./App.css";
import store from "./store";
import {BrowserRouter as Router, Route} from "react-router-dom";
import {Provider} from "react-redux";
import Fileboard from "./components/Fileboard";
import Result from "./components/Result";

function App() {
    return (
        <Provider store={store}>
            <Router>
                <div className="App">
                    <Route exact path="/files" component={Fileboard}/>
                    <Route exact path="/file/:identifier" component={Result}/>
                </div>
            </Router>
        </Provider>
    );
}

App.propTypes = {
    classes: PropTypes.object.isRequired
};

export default App;
