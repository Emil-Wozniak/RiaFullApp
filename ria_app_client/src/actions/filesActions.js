import axios from "axios";
import * as types from "./types";

export const getFiles = () => async dispatch => {
  const res = await axios.get("http://localhost:8080/api/files/all");
  dispatch({
    type: types.GET_FILE_ENTITIES,
    payload: res.data
  });
};

export const getFile = filename => async dispatch => {
  const { data } = await axios.get(`http://localhost:8080/api/files/${filename}`);
  dispatch({
      type: types.GET_FILE_ENTITY,
      payload: data
  });
};

export const confirmDeleteFile = file_entity_id => window.confirm(`You are deleting file_entity task ${file_entity_id}, this action cannot be undone`)
  if (confirmDeleteFile()) {}
