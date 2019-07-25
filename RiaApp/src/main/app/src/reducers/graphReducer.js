import * as types from "../actions/types";

const initialState = {
  graph: [],
  graph_lines: []
};

export default function(state = initialState, action) {
  switch (action.type) {
    case types.GET_GRAPH:
      return {
        ...state,
        graph: action.payload
      };
    case types.GET_GRAPH_LINE:
      return {
        ...state,
        graph_lines: action.payload
      };

    default:
      return state;
  }
}
