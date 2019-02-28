import * as types from "../actions/types";

const initialState = {
  results: [],
  result: {},
  control_curves: [],
  control_curve: {}
};

export default function(state = initialState, action) {
  switch (action.type) {
    case types.GET_BACKLOG:
      return {
        ...state,
        results: action.payload
      };

      case types.GET_BACKLOG_CC:
      return {
        ...state,
        control_curves: action.payload
      };

    case types.GET_RESULT:
      return {
        ...state,
        result: action.payload
      };

    case types.GET_CONTROL_CURVE:
      return {
        ...state,
        result: action.payload
      };

    default:
      return state;
  }
}
