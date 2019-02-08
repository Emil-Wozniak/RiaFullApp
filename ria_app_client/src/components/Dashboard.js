import React, { Component } from "react";
import compose from "recompose/compose";
import { connect } from "react-redux";
import { getFiles } from "../actions/filesActions";
import PropTypes from "prop-types";
import { Col, Row, Container, Table } from "reactstrap";
import FileEntity from "./layout/File_Entity/FileEntity";
import { withStyles } from "@material-ui/core/styles";
import Paper from "@material-ui/core/Paper";

const styles = theme => ({
  root: {
    flexGrow: 1
  },
  demo: {
    height: 240
  },
  paper: {
    padding: theme.spacing.unit * 2,
    height: "100%",
    color: theme.palette.text.secondary
  },
  control: {
    padding: theme.spacing.unit * 2
  }
});

class Dashboard extends Component {
  state = {
    direction: "row",
    justify: "center",
    alignItems: "center"
  };

  handleChange = key => (event, value) => {
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
      <Container>
        <Row>
          <Col md={12}>
            <br />
            <Paper classes={{paper:"paper"}}>
            <h5 className="text-center">Files</h5>
            <Table striped>
              <thead>
                <tr>
                  <th>#</th>
                  <th />
                  <th>File Name</th>
                  <th>Content Type:</th>
                  <th>Uploaded</th>
                  <th/>
                </tr>
              </thead>
              {file_entities.map(file_entity => (
                <FileEntity key={file_entity.id} file_entity={file_entity} />
              ))}
            </Table>
            </Paper>
          </Col>
        </Row>
      </Container>
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
  withStyles(styles),
  connect(
    mapStateToProps,
    { getFiles }
  )
)(Dashboard);
