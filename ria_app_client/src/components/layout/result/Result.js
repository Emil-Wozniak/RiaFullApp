import React, { Component } from "react";
import compose from "recompose/compose";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import { deleteFileEntity } from "../../../actions/filesActions";
import { withStyles } from "@material-ui/core/styles";

import Avatar from "@material-ui/core/Avatar";
import FolderIcon from "@material-ui/icons/Folder";

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

class Result extends Component {
  render() {
    const { result } = this.props;

    return (
      <React.Fragment>
        <tbody>
          <tr>
            <th scope="row">{result.id}</th>
            <td>
              <Avatar>
                <FolderIcon />
              </Avatar>
            </td>
            <td> {result.fileName}</td>
            <td>{result.contentType}</td>
            <td>{result.created_date}</td>
            <td>
            </td>
          </tr>
        </tbody>
      </React.Fragment>
    );
  }
}

Result.propTypes = {
  classes: PropTypes.object.isRequired,
  result: PropTypes.object.isRequired,
  deleteFileEntity: PropTypes.func.isRequired
};

export default compose(
  withStyles(styles),
  connect(
    null,
    { deleteFileEntity }
  )
)(Result);
