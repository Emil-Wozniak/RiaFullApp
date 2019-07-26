import React from 'react';
import "./App.css";
import store from "./store";
import {BrowserRouter as Router, Route} from "react-router-dom";
import {Provider} from "react-redux";
import Fileboard from "./components/Fileboard";
import Result from "./components/Result";
import Darkmode from "darkmode-js"

function App() {
    const options = {
        bottom: '64px',
        right: '32px',
        left: 'unset',
        time: '0.5s',
        mixColor: '#fff',
        backgroundColor: '#fff',
        buttonColorDark: '#100f2c',
        buttonColorLight: '#fff',
        saveInCookies: true,
        label: '🌓'
    };
    const darkmode = new Darkmode(options);
    darkmode.showWidget();

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


export default App;
