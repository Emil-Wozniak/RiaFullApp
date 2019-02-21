import {
  GET_FILE_ENTITIES, GET_ERRORS
} from "../actions/types";

const initialState = {
  file_entities: [],
  file_entity:{},
  errors: {}
};

export default function(state = initialState, action) {
  switch (action.type) {
    case GET_FILE_ENTITIES:
      return {
        ...state,
        file_entities: action.payload
      };
      case GET_ERRORS:
      return {
        ...state,
        errors: action.message
      }

    default:
      return state;
  }
}
