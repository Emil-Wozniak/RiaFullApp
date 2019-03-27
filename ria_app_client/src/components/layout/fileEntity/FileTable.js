import React, { Component } from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import PropTypes from "prop-types";
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles";
import { getFiles } from "../../../actions/filesActions";
import { withStyles } from "@material-ui/core/styles";
import IconButton from "@material-ui/core/IconButton";
import { DeleteForeverRounded } from "@material-ui/icons";
import BootstrapTable from "react-bootstrap-table-next";

const theme = createMuiTheme({
    typography: {
      useNextVariants: true
    },
    palette: {
      primary: {
        main: "#fafafa"
      }
    }
  });

const formatWithIcon = (cell, row) => {
  return (
    <span>
      <IconButton>
        <DeleteForeverRounded />
      </IconButton>
    </span>
  );
};

const columns = [
  {
    dataField: "fileName",
    text: "File name"
  },
  {
    dataField: "contentType",
    text: "Type"
  },
  {
    dataField: "created_date",
    text: "Uploaded"
  },
  {
    dataField: "date",
    text: "icon",
    formatter: formatWithIcon
  }
];

class FileTable extends Component {
  render() {
    const { file_entities } = this.props.file_entity;
    return (
      <React.Fragment>
        <BootstrapTable keyField="id" data={file_entities} columns={columns} />
      </React.Fragment>
    );
  }
}

FileTable.propTypes = {
    classes: PropTypes.object.isRequired,
    file_entity: PropTypes.object.isRequired,
    getFiles: PropTypes.func.isRequired
  };
  
  const mapStateToProps = state => ({
    file_entity: state.file_entity
  });
  
  export default compose(
    withStyles(theme),
    connect(
      mapStateToProps,
      { getFiles }
    )
  )(FileTable);
