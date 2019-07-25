import axios from "axios";
import * as types from "./types";

const LOCAL_HOST = "http://localhost:8080/api";

export const getExaminationPoints = () => async dispatch => {
    const {data} = await axios.get(LOCAL_HOST + "/examination/all/");
    dispatch({
        type: types.GET_EXAMINATION_POINTS,
        payload: data
    })
};
