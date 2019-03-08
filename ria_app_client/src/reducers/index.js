import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import fileEntityReducer from "./fileEntityReducer";
import backlogReducer from "./backlogReducer";
import graphCurveReducer from "./graphCurveReducer";

export default combineReducers({
  errors: errorReducer,
  file_entity: fileEntityReducer,
  backlog: backlogReducer,
  graph_curve: graphCurveReducer
});
