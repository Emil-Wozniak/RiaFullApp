import React, {Component} from 'react';
import {connect} from "react-redux";
import {getExaminationPoints} from "../actions/examinationPointActions";
import PropTypes from "prop-types";
import Paper from "@material-ui/core/Paper";
import {Col, Container, Table} from "reactstrap";
import ExaminationPoint from "./models/examination_point/ExaminationPoint";
import UploadFile from "../request/upload/UploadFile";

class Fileboard extends Component {

    componentDidMount() {
        this.props.getExaminationPoints();
    }

    render() {
        const {examination_points} = this.props.examination_point;
        const files = examination_points.map(examination_point =>
            (<ExaminationPoint key={examination_point.id}
                               examination_point={examination_point}/>));

        return (
            <React.Fragment>
                <UploadFile/>
                <br/>
                <Container>
                    <Paper>
                        <Col className={"FileBoard-Title"}>
                            <h4>Files:</h4>
                        </Col>
                        <hr/>
                        <Table striped id="file_data">
                            <thead>
                            <tr>
                                <th/>
                                <th>filename:</th>
                                <th>pattern:</th>
                            </tr>
                            </thead>
                            {files}
                        </Table>
                    </Paper>
                </Container>
            </React.Fragment>

        );
    }
}

Fileboard.propTypes = {
    classes: PropTypes.object.isRequired,
    examination_point: PropTypes.object.isRequired,
    getExaminationPoints: PropTypes.func.isRequired
};

const mapStateToProps = state => ({
    examination_point: state.examination_point
});

export default connect(mapStateToProps, {getExaminationPoints})(Fileboard)
