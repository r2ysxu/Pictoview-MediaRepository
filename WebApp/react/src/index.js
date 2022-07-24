import React from 'react';
import ReactDOM from 'react-dom';
import {
    BrowserRouter as Router,
    Switch,
    Route
  } from "react-router-dom";
import { Provider } from 'react-redux';
import store from './model/store';
import Home from './page/Home';
import AlbumPage from './page/AlbumPage';
import AdminManager from './page/AdminManager';


function App() {
  return(
      <Router>
      <Switch>
        <Route path="/album" component={AlbumPage} />
        <Route path="/admin" component={AdminManager} />
        <Route path="/" component={Home} />
      </Switch>
      </Router>
  );
}

ReactDOM.render(
  <Provider store={store}>
    <App />
  </Provider>,
  document.getElementById("root")
);