import React from 'react';
import ReactDOM from 'react-dom';
import {
    BrowserRouter as Router,
    Switch,
    Route
  } from "react-router-dom";
import Home from './page/Home';
import Album from './page/Album';
import AdminManager from './page/AdminManager';


function App() {
  return(
      <Router>
      <Switch>
        <Route path="/album" component={Album} />
        <Route path="/admin" component={AdminManager} />
        <Route path="/" component={Home} />
      </Switch>
      </Router>
  );
}

ReactDOM.render(
    <App />,
  document.getElementById("root")
);