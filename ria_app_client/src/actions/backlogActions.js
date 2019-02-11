import axios from "axios";
import * as types from "./types";

//Fix bug with priority in Spring Boot Server, needs to check null first

export const getBacklog = dataId => async dispatch => {
  try {
    const { data } = await axios.get(`/api/backlog/${dataId}`);
    dispatch({
      type: types.GET_BACKLOG,
      payload: data
    });
  } catch (err) {
    dispatch({
      type: types.GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const getResult = (dataId, resultId, history) => async dispatch => {
  try {
    const { data } = await axios.get(`/api/backlog/${dataId}/${resultId}`);
    dispatch({
      type: types.GET_RESULT,
      payload: data
    });
  } catch (err) {
    history.push("/dashboard");
  }
};
