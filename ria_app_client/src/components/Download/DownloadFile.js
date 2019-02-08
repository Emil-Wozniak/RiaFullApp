import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { getFiles } from "../../actions/filesActions";
import { FileService } from "../Service/FileService";
import { Button } from "reactstrap";
import { saveAs } from "file-saver";

class DownloadFile extends Component {
  constructor(props) {
    super(props);
    this.fileService = new FileService();
    this.state = {
      downloading: false
    };
  }

  downloadFile = () => {
    this.setState({
      downloading: true
    });

    var filename = this.props.fileName;
    let self = this;
    this.fileService
      .getFileFromServer()
      .then(response => {
        console.log("Response", response);
        this.setState({ downloading: false });
        //extract file name from Content-Disposition header

        //invoke 'Save As' dialog
        saveAs(response.data, filename);
      })
      .catch(function(error) {
        console.log(error);
        self.setState({ downloading: false });
        if (error.response) {
          console.log("Error", error.response.status);
        } else {
          console.log("Error", error.message);
        }
      });
  };

  render() {
    return (
      <React.Fragment>
        <Button className="btn" onClick={this.downloadFile}>
          Download file{" "}
        </Button>
        <label>{this.state.downloading ? "Downloading in progress" : ""}</label>
      </React.Fragment>
    );
  }
}

DownloadFile.propTypes = {
  file_entity: PropTypes.object.isRequired,
  getFiles: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
  file_entity: state.file_entity
});

export default connect(
  mapStateToProps,
  { getFiles }
)(DownloadFile);
