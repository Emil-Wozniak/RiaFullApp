import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { Container } from "reactstrap";
import { withStyles } from "@material-ui/core/styles";
import { getFiles } from "../../actions/filesActions";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import Typography from "@material-ui/core/Typography";
import green from "@material-ui/core/colors/green";
import Loading from "../../components/layout/ui/Loading";

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
    msg: "",
    error: "",
    image: false,
    direction: "row",
    justify: "center",
    alignItems: "center"
  };

  componentDidMount() {
    this.props.getFiles();
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

    let data = new FormData();
    data.append("file", this.state.file);
    data.append("name", this.state.file.name);

    
    fetch("http://localhost:8080/api/files", {
      method: "POST",
      body: data
    })
      .then(response => {
        this.setState({
          error: "",
          msg: "Successfully uploaded file",
          image: false
        });
        window.location.reload()
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
        <br />
        <Paper classes={{ paper: "paper" }}>
          <TabContainer dir={theme.direction}>
            <input onChange={this.onFileChange} type="file" />
            <Button
              variant="contained"
              color="default"
              classes={{ button: "button" }}
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

AddFile.propTypes = {
  file_entity: PropTypes.func.isRequired,
  classes: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
  file_entity: state.file_entity
});

export default compose(
  withStyles(styles, { withTheme: true }),
  connect(
    mapStateToProps,
    { getFiles }
  )
)(AddFile);
