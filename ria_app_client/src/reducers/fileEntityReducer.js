import {
  GET_FILE_ENTITIES
} from "../actions/types";

const initialState = {
  file_entities: [],
  file_entity:{}
};

export default function(state = initialState, action) {
  switch (action.type) {
    case GET_FILE_ENTITIES:
      return {
        ...state,
        file_entities: action.payload
      };

    default:
      return state;
  }
}
