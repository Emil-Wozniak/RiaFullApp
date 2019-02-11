import React, { Component } from "react";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { FileService } from "../service/FileService";
import { getFiles } from "../../actions/filesActions";
import { Button } from "reactstrap";
import { saveAs } from "file-saver";

class DownloadFile extends Component {
  constructor(props) {
    super(props);
    this.fileService = new FileService();
    this.state = {
      downloading: false,
      fileName: ""
    };
  }

  componentDidMount() {
    const { fileName } = this.props;
    this.props.getFiles(fileName, this.props.history);
  }


  downloadFile = () => {
    this.setState({
      downloading: true,
      fileName: this.props.fileName
    });

    const { file_entity } = this.props;
    // var filename = this.props.fileName;
    const filename = "file.txt";
    const name = file_entity.fileName;

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
          console.log("Error", error.response.status, name);
        } else {
          console.log("Error", error.message);
        }
      });
  };

  render() {
    return (
      <React.Fragment>
        <Button className="btn fa fa-file-export" onClick={this.downloadFile}>
          {" "}
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
