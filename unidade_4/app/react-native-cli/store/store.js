import { createStore } from 'redux';

const initialState = {
    loggedIn: false
}

const reducer = (state = initialState, action) => {
  let result;
  switch (action.type) {
      case 'LOGIN':
        result = { loggedIn: true };
        break;
      default:
        result = { loggedIn: false };
        break;
  }
  return result;
}

const store = createStore(reducer);

export default store;
