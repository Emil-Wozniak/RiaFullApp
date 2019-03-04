import axios from "axios";
import * as types from "./types";

export const getGraphCurve = dataId => async dispatch => {
    try {
      const {data} = await axios.get(`/api/graph/${dataId}`);
      dispatch({
        type: types.GET_GRAPH_CURVE,
        payload: data
      });
    } catch (err) {
      dispatch({
        type: types.GET_ERRORS,
        payload: err.response.data
      });
    }
  };