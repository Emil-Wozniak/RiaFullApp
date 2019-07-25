import axios from "axios";
import * as types from "./types";

const LOCAL_HOST = "http://localhost:8080/api";

export const getGraph = identifier => async dispatch => {
    const {data} = await axios.get(LOCAL_HOST + `/graph/${identifier}`);
    dispatch({
        type: types.GET_GRAPH,
        payload: data
    });
};

export const getGraphLines = identifier => async dispatch => {
    const {data} = await axios.get(LOCAL_HOST + `/graph/lines/${identifier}`);
    dispatch({
        type: types.GET_GRAPH_LINE,
        payload: data
    });
};
