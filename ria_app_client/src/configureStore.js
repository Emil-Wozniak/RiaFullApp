import {createStore, applyMiddleware} from 'redux';  
import rootReducer from '../reducers/rootReducer';  
import thunk from 'redux-thunk';
import { composeWithDevTools } from "redux-devtools-extension";

export default function configureStore() {  
    const composeEnhancers = composeWithDevTools({
        // Specify name here, actionsBlacklist, actionsCreators and other options if needed
      });
  return createStore(
    rootReducer,
    applyMiddleware(thunk)
  );
}