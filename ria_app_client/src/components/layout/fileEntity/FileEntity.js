import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { deleteFileEntity } from "../../../actions/filesActions";
import CustomDownloadFile from "../../../requests/download/CustomDownloadFile";
import { withStyles } from "@material-ui/core/styles";
import { Link } from "react-router-dom";
import Avatar from "@material-ui/core/Avatar";
import FolderIcon from "@material-ui/icons/Folder";
import IconButton from "@material-ui/core/IconButton";
import {
  InsertDriveFileRounded,
  DeleteForeverRounded
} from "@material-ui/icons";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  paper: {
    padding: theme.spacing.unit * 2,
    textAlign: "center",
    color: theme.palette.text.secondary
  }
});

class FileEntity extends Component {
  onDeleteClick = id => {
    this.props.deleteFileEntity(id);
  };

  render() {
    const { file_entity } = this.props;

    return (
      <React.Fragment>
        <tbody>
          <tr>
            <td>
              <Link to={`/fileBoard/${file_entity.dataId}`}>
                <IconButton>
                  <Avatar>
                    <FolderIcon />
                  </Avatar>
                </IconButton>
              </Link>
            </td>
            <td> {file_entity.fileName}</td>
            <td>{file_entity.contentType}</td>
            <td>{file_entity.created_date}</td>
            <td>
              <CustomDownloadFile />
              <IconButton
                onClick={this.onDeleteClick.bind(this, file_entity.id)}>
                <DeleteForeverRounded />
              </IconButton>
            </td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

FileEntity.propTypes = {
  classes: PropTypes.object.isRequired
};

export default compose(
  withStyles(styles),
  connect(
    null,
    { deleteFileEntity }
  )
)(FileEntity);
