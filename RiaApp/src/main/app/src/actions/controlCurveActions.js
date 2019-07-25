import axios from "axios";
import * as types from "./types";

export const getControlCurve = identifier => async dispatch => {
    const {data} = await axios.get(`http://localhost:8080/api/control_curve/${identifier}`);
    console.log("ok");
    dispatch({
        type: types.GET_CONTROL_CURVES,
        payload: data
    });
};
