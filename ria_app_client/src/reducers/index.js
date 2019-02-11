import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import fileEntityReducer from "./fileEntityReducer";
import backlogReducer from "./backlogReducer"

export default combineReducers({
  errors: errorReducer,
  file_entity: fileEntityReducer,
  backlog: backlogReducer,
});
