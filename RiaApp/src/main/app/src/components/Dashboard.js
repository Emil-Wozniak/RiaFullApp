import React, {Component} from 'react';
import {connect} from "react-redux";
import {getExaminationPoints} from "../actions/examinationPointActions";
import PropTypes from "prop-types";
import Paper from "@material-ui/core/Paper";
import {Col, Container, Row, Table} from "reactstrap";

class Dashboard extends Component {

    componentWillMount() {
        this.props.getExaminationPoints();
    }

    render() {
        return (
            <Container>
                <Row>
                    <Col md={12}>
                        <br/>
                        <Paper>
                            <Col>
                                <p className="text-left">Files:</p>
                            </Col>
                            <Table striped>
                                <thead>
                                <tr>
                                    <th/>
                                    <th>File Name</th>
                                    <th>Content Type:</th>
                                    <th/>
                                </tr>
                                </thead>
                                {/*{file_entities*/}
                                {/*    .sort((a, b) => a.id < b.id)*/}
                                {/*    .map(file_entity => (*/}
                                {/*        <FileEntity*/}
                                {/*            key={file_entity.id}*/}
                                {/*            file_entity={file_entity}*/}
                                {/*        />*/}
                                {/*    ))}*/}
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
    examination_point: PropTypes.object.isRequired,
    getExaminationPoints: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    file_entity: state.file_entity
});

export default connect(mapStateToProps, {getExaminationPoints})(Dashboard)
