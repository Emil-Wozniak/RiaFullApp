import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { Container } from "reactstrap";
import { withStyles } from "@material-ui/core/styles";
import Grid from "@material-ui/core/Grid";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import Tabs from "@material-ui/core/Tabs";
import Tab from "@material-ui/core/Tab";
import Typography from "@material-ui/core/Typography";
import green from "@material-ui/core/colors/green";

const styles = theme => ({
  root: {
    flexGrow: 1,
    display: "flex",
    justifyContent: "center",
    alignItems: "flex-end"
  },
  paper: {
    padding: theme.spacing.unit * 2,
    margin: "auto",
    maxWidth: 500
  },
  button: {
    margin: theme.spacing.unit
  },
  leftIcon: {
    marginRight: theme.spacing.unit
  },
  rightIcon: {
    marginLeft: theme.spacing.unit
  },
  iconSmall: {
    fontSize: 20
  },
  fab: {
    position: "absolute",
    bottom: theme.spacing.unit * 2,
    right: theme.spacing.unit * 2
  },
  fabGreen: {
    color: theme.palette.common.white,
    backgroundColor: green[500],
    "&:hover": {
      backgroundColor: green[600]
    }
  }
});

function TabContainer(props) {
  const { children, dir } = props;

  return (
    <Typography component="div" dir={dir} style={{ padding: 8 * 3 }}>
      {children}
    </Typography>
  );
}

class AddFile extends Component {
  state = {
    file: "",
    error: "",
    msg: "",
    direction: "row",
    justify: "center",
    alignItems: "center"
  };

  handleChange = key => (event, value) => {
    this.setState({
      [key]: value
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
    }

    let data = new FormData();
    data.append("file", this.state.file);
    data.append("name", this.state.file.name);

    fetch("http://localhost:8080/api/files", {
      method: "POST",
      body: data
    })
      .then(response => {
        this.setState({ error: "", msg: "Successfully uploaded file" });
      })
      .catch(err => {
        this.setState({ error: err });
      });
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
        <Paper classes={{ paper: "paper" }}>
          <Tabs textColor="primary" variant="fullWidth">
            <Tab label="Upload a file" />
          </Tabs>
          <Grid container wrap="nowrap" spacing={16}>
            <TabContainer dir={theme.direction}>
              <h4 style={{ color: "red" }}>{this.state.error}</h4>
              <h4 style={{ color: "green" }}>{this.state.msg}</h4>
              <input onChange={this.onFileChange} type="file" />
              <Button
                variant="contained"
                color="default"
                classes={{ button: "button" }}
                onClick={this.uploadFile}
              >
                {" "}
                Upload
                <CloudUploadIcon classes={{ rightIcon: "rightIcon" }} />
              </Button>
            </TabContainer>
          </Grid>
        </Paper>
      </Container>
    );
  }
}

AddFile.propTypes = {
  classes: PropTypes.object.isRequired,
  theme: PropTypes.object.isRequire
};

export default compose(withStyles(styles, { withTheme: true }))(AddFile);
