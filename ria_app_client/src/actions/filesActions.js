import axios from "axios";
import * as types from "./types";

export const getFiles = () => async dispatch => {
  const {data} = await axios.get("http://localhost:8080/api/files/all");
  dispatch({
    type: types.GET_FILE_ENTITIES,
    payload: data
  });
};

export const getFile = filename => async dispatch => {
  const { data } = await axios.get(`http://localhost:8080/api/files/${filename}`);
  dispatch({
      type: types.GET_FILE_ENTITY,
      payload: data
  });
};

const confirmDeleteFile = file_entity_id => window.confirm(`You are deleting file ${file_entity_id}, this action cannot be undone`);

export const deleteFileEntity = file_entity_id => async dispatch => {
    if (confirmDeleteFile(file_entity_id)) {
    await axios.delete(`http://localhost:8080/api/files/${file_entity_id}`);
    dispatch({
      type: types.DELETE_FILE_ENTITY,
      payload: file_entity_id
    });
  }
};
