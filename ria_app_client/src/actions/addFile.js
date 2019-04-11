import axios from "axios";

export const addFile = file => {
  let formData = new FormData();
  formData.append("file", file);
  formData.append("name", file.name);

  return axios.post(`/api/files`, formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
    .then(response => {
      this.setState({
        error: "",
        msg: "File uploaded",
        image: false
      });
    })
    .catch(error => {
      this.setState({
        error: error
      });
      this.errors.push(error);
    });
};
