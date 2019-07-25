import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import examinationPointReducer from "./examinationPointReducer";
import controlCurveReducer from "./controlCurveReducer";

export default combineReducers({
    control_curve: controlCurveReducer,
    examination_point: examinationPointReducer,
    errors: errorReducer
});
