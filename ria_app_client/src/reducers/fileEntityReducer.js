import {
  GET_FILE_ENTITIES, GET_ERRORS, DELETE_FILE_ENTITY
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
      case DELETE_FILE_ENTITY:
      return {
        ...state,
        file_entities: state.file_entities.filter(
          file_entity => file_entity.id !== action.payload
        )
      };

    default:
      return state;
  }
}
