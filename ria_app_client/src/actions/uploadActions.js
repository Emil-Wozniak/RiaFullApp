import axios from "axios";
import { GET_ERRORS } from "../actions/types";

export const uploadFile = (data) => async dispatch => {
  try {
    await axios.fetch(`http://localhost:8080/api/files`, data);
    dispatch({
      type: GET_ERRORS,
      payload: {},
      method: "POST",
      body: data
    })
      .then(response => {
        this.setState({ error: "", msg: "Successfully uploaded file" });
      })
  } catch (err) {
    dispatch({
      type: GET_ERRORS,
      payload: err.response.data
    });
  }
};
