import axios from "axios";
import * as types from "./types";

const LOCAL_HOST = "http://localhost:8080/api";

export const getExaminationPoints = () => async dispatch => {
    try {
        const {data} = await axios.get("http://localhost:8080/api/examination/");
        dispatch({
            type: types.GET_EXAMINATION_POINTS,
            payload: data
        })
    } catch (error) {
        dispatch({
            type: types.GET_ERRORS,
            payload: error.response.data
        })
    }
};
