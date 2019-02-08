import axios from "axios";
import { DELETE_FILE_ENTITY, GET_FILE_ENTITIES, GET_FILE_ENTITY } from "./types";

export const getFiles = () => async dispatch => {
  const res = await axios.get("http://localhost:8080/api/files/all");
  dispatch({
    type: GET_FILE_ENTITIES,
    payload: res.data
  });
};

export const getFile = filename => async dispatch => {
  
    const res = await axios.get(`http://localhost:8080/api/files/${filename}`);
    dispatch({
      type: GET_FILE_ENTITY,
      payload: res.data
    });
  
};

export const deleteFileEntity = file_entity_id => async dispatch => {
  if (
    window.confirm(
      `You are deleting file_entity task ${file_entity_id}, this action cannot be undone`
    )
  ) {
    await axios.delete(`/api/files/${file_entity_id}`);
    dispatch({
      type: DELETE_FILE_ENTITY,
      payload: file_entity_id
    });
  }
};
