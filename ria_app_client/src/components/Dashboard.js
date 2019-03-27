import React, { Component } from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { getFiles } from "../actions/filesActions";
import PropTypes from "prop-types";
import { Col, Row, Container, Table } from "reactstrap";
import { withStyles } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles";
import FileEntity from "./layout/fileEntity/FileEntity";
import UploadFile from "../components/layout/requests/upload/UploadFile";
import FileTable from "./layout/fileEntity/FileTable";

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

class Dashboard extends Component {
  state = {
    direction: "row",
    justify: "center",
    alignItems: "center"
  };

  handleChange = key => value => {
    this.setState({
      [key]: value
    });
  };

  componentDidMount() {
    this.props.getFiles();
  }

  render() {
    const { file_entities } = this.props.file_entity;

    return (
      <MuiThemeProvider theme={theme}>
        <React.Fragment>
          <UploadFile />
          <Container>
            <Row>
              <Col md={12}>
                <br />
                <Paper>
                  <Col>
                    <p className="text-left">Files:</p>
                  </Col>
                  <Table striped>
                    <thead>
                      <tr>
                        <th />
                        <th>File Name</th>
                        <th>Content Type:</th>
                        <th>Uploaded</th>
                        <th />
                      </tr>
                    </thead>
                    {file_entities
                      .sort((a, b) => a.id < b.id)
                      .map(file_entity => (
                        <FileEntity
                          key={file_entity.id}
                          file_entity={file_entity}
                        />
                      ))}
                  </Table>
                </Paper>
              </Col>
            </Row>
          </Container>
        </React.Fragment>
      </MuiThemeProvider>
    );
  }
}

Dashboard.propTypes = {
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
)(Dashboard);
