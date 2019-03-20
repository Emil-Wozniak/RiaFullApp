import React, { Component } from "react";
import { Container } from "reactstrap";
import { withStyles } from "@material-ui/core/styles";
import { createMuiTheme } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import Loading from "../../ui/Loading";
import {addFile} from "../../../../actions/addFile"

const styles = theme => createMuiTheme({
   typography: {
      useNextVariants: true,
    },
  root: {
    flexGrow: 1,
    display: "flex",
    justifyContent: "center",
    alignItems: "flex-end"
  },
});

function TabContainer(props) {
  const { children } = props;

  return (
    <div>
      <br />
      {children}
      <br />
    </div>
  );
}

class AddFile extends Component {
  constructor() {
    super();
    this.state = {
      file: "",
      msg: "",
      error: "",
      filename: false,
      image: false,
      errors: {}
    };
  }


  handleChange = key => (event, value) => {
    this.setState({
      [key]: value,
      image: false
    });
  };

  uploadFile = event => {
    event.preventDefault();

    this.setState({ error: "", msg: "" });

    if (!this.state.file) {
      this.setState({ error: "Please upload a file." });
      return;
    }
    if (this.state.file.size >= 2000000) {
      this.setState({ error: "File size exceeds limit of 2MB." });
      return;
    } else {
      this.setState({
        image: true
      });
    }
    addFile(this.state.file);
  };

  onFileChange = event => {
    this.setState({
      file: event.target.files[0]
    });
  };

  render() {
    const { theme } = this.props;

    return (
      <Container>
        <br />
        <Paper>
          <TabContainer dir={theme.direction}>
            <input onChange={this.onFileChange} type="file" />
            <Button
              variant="contained"
              color="default"
              onClick={this.uploadFile}
            >
              <CloudUploadIcon />
            </Button>
            <Container>
              <p style={{ textAlign: "center", color: "red" }}>
                {this.state.error}
              </p>
              <p style={{ textAlign: "center", color: "green" }}>
                {this.state.msg}
              </p>
              {this.state.image ? <Loading /> : null}
            </Container>
          </TabContainer>
        </Paper>
      </Container>
    );
  }
}


export default
  withStyles(styles, { withTheme: true })(AddFile);
