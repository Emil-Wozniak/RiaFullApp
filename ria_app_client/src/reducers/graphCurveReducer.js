import * as types from "../actions/types";

const initialState = {
  graph_curves: [],
  graph_curve: {}
};

export default function(state = initialState, action) {
  switch (action.type) {
    case types.GET_GRAPH_CURVE:
      return {
        ...state,
        graph_curves: action.payload
      };
    case types.GET_GRAPH_CURVE_POINT:
      return {
        ...state,
        graph_curve: action.payload
      };

    default:
      return state;
  }
}
