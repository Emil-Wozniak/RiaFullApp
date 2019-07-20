import axios from "axios";
import * as types from "./types";

const LOCAL_HOST = "http://localhost:8080/api";

export const getControlCurve = (fileName, history) => async dispatch => {
    try {
        const {data} = await axios.get(LOCAL_HOST + `/api/backlog/${fileName}`);
        dispatch({
            type: types.GET_CONTROL_CURVE,
            payload: data
        });
    } catch (err) {
        history.push("/dashboard");
    }
};
