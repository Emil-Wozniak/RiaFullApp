import { combineReducers } from "redux";
import errorReducer from "./errorReducer";
import examinationPointReducer from "./examinationPointReducer";
import controlCurveReducer from "./controlCurveReducer";
import graphReducer from "./graphReducer";

export default combineReducers({
    control_curve: controlCurveReducer,
    examination_point: examinationPointReducer,
    graph: graphReducer,
    errors: errorReducer
});
