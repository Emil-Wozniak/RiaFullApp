import * as types from "../actions/types";
  
  const initialState = {
    results: [],
    result: {}
  };
  
  export default function(state = initialState, action) {
    switch (action.type) {
      case types.GET_BACKLOG:
        return {
          ...state,
          results: action.payload
        };
  
      case types.GET_RESULT:
        return {
          ...state,
          result: action.payload
        };
  
      default:
        return state;
    }
  }
  