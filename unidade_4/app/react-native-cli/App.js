import React, { Component } from 'react';
import { Provider } from 'react-redux';
import store from './store/store';
import RouterApp from './components/RouterApp';


export default class App extends Component {

  render() {
    return (
      <Provider store={store}>
        <RouterApp />
      </Provider>
    )
  }

}