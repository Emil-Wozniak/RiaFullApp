import axios from "axios";
import * as types from "./types";

export const getBacklog = dataId => async dispatch => {
  try {
    const {data} = await axios.get(`/api/backlog/${dataId}`);
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

export const getBacklogWithCC = dataId => async dispatch => {
  try {
    const {data} = await axios.get(`/api/backlog/${dataId}/curve`);
    dispatch({
      type: types.GET_BACKLOG_CC,
      payload: data
    });
  } catch (err) {
    dispatch({
      type: types.GET_ERRORS,
      payload: err.response.data
    });
  }
};

export const getControlCurve = (dataId, fileName, history) => async dispatch => {
  try {
    const { data } = await axios.get(
      `/api/backlog/${dataId}/${fileName}`
    );
    dispatch({
      type: types.GET_CONTROL_CURVE,
      payload: data
    });
  } catch (err) {
    history.push("/dashboard");
  }
};

export const getResult = (dataId, fileName, history) => async dispatch => {
  try {
    const { data } = await axios.get(
      `/api/backlog/${dataId}/curve/${fileName}`
    );
    dispatch({
      type: types.GET_RESULT,
      payload: data
    });
  } catch (err) {
    history.push("/dashboard");
  }
};
