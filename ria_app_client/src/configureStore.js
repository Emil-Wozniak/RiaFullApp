import { createStore } from 'redux';
import rootReducer from '../reducers';

export default function configureStore(initialState) {
  let store = (window.devToolsExtension ? window.devToolsExtension()(createStore) : createStore)(rootReducer, initialState);

  if (module.hot) {
    // Enable Webpack hot module replacement for reducers
    module.hot.accept('../reducers', () => {
      const nextReducer = require('../reducers');
      store.replaceReducer(nextReducer);
    });
  }

  return store;
}
