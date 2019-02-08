import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import fileEntityReducer from "./fileEntityReducer";

export default combineReducers({
  errors: errorReducer,
  file_entity: fileEntityReducer
});
