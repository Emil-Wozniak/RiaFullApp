import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import examinationPointReducer from "./examinationPointReducer";

export default combineReducers({
    examination_point: examinationPointReducer,
    errors: errorReducer
});
