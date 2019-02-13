import axios from "axios";

export const uploadFile = (data, history) => async dispatch => {
  const { data } = await axios
    .fetch(`http://localhost:8080/api/files`, {
      method: "POST",
      body: data
    })
    .then(response => {
      this.setState({ error: "", msg: "Successfully uploaded file" });
      history.push("/dashboard");
    })
    .catch(err => {
      this.setState({ error: err });
      history.push("/dashboard");
    });
};
