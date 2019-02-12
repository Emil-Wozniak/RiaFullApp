import axios from "axios";
import * as types from "./types";

export const getBacklog = dataId => async dispatch => {
  try {
    const res = await axios.get(`http://localhost:8080/api/backlog/${dataId}`);
    dispatch({
      type: types.GET_BACKLOG,
      payload: res.data
    });
  } catch (err) {
    dispatch({
      type: types.GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const getResult = (
  dataId, 
  fileName, 
  history) => async dispatch => {
  try {
    const { data } = await axios.get(`http://localhost:8080/api/backlog/${dataId}/${fileName}`);
    dispatch({
      type: types.GET_RESULT,
      payload: data
    });
  } catch (err) {
    history.push("/dashboard");
  }
};
