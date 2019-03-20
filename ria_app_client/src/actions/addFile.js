import axios from "axios";
import * as types from "./types";

export const addFile = file => {
  let formData = new FormData();
  formData.append("file", file);
  formData.append("name", file.name);

  return axios
    .post(`/api/files`, formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    })
    .then(response => {
      this.setState({
        error: "",
        msg: response.body.message,
        image: false
      });
      window.location.reload();
    })
    .catch(error => {
      this.errors.push(error);
    });
};

// let data = new FormData();
// data.append("file", this.state.file);
// data.append("name", this.state.file.name);
// fetch("http://localhost:8080/api/files/", {
//   method: "POST",
//   body: data
// })
//   .then(response => {
//     this.setState({
//       error: "",
//       msg: response.body.message,
//       image: false
//     });
//     window.location.reload();
//   })
//   .catch(error => {
//     console.log(error)
//     this.setState({ error: error });
//   });
