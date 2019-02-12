import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { deleteFileEntity } from "../../../actions/filesActions";
import DownloadFile from "../../../requests/download/DownloadFile";
import { withStyles } from "@material-ui/core/styles";
import { Link } from "react-router-dom";
import Avatar from "@material-ui/core/Avatar";
import FolderIcon from "@material-ui/icons/Folder";
import { Button } from "reactstrap";

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
  render() {
    const { file_entity } = this.props;

    return (
      <React.Fragment>
        <tbody>
          <tr>
            <th scope="row">{file_entity.id}</th>
            <td>
              <Avatar>
                <FolderIcon />
              </Avatar>
            </td>
            <td> {file_entity.fileName}</td>
            <td>{file_entity.contentType}</td>
            <td>{file_entity.created_date}</td>
            <td>
              <DownloadFile />
            </td>
            <td>
              {" "}
              <Link to={`/fileBoard/${file_entity.dataId}`}>
                <Button className="btn fa fa-file"></Button>
              </Link>
            </td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

FileEntity.propTypes = {
  classes: PropTypes.object.isRequired,
};

export default compose(
  withStyles(styles),
  connect(
    null,
    { deleteFileEntity }
  )
)(FileEntity);
