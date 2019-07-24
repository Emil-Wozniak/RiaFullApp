import React, {Component} from 'react';
import {connect} from "react-redux";
import {getExaminationPoints} from "../actions/examinationPointActions";
import PropTypes from "prop-types";
import Paper from "@material-ui/core/Paper";
import {Col, Container, Row} from "reactstrap";
import ExaminationPoint from "./examinationPoint/ExaminationPoint";

class Dashboard extends Component {

    componentDidMount() {
        this.props.getExaminationPoints();
    }

    render() {
        const {examination_points} = this.props.examination_point;

        return (
            <Container>
                <br/>
                <Row>
                    <Col md={12}>
                        <br/>
                        <Paper>
                            <Col>
                                <p className="text-left">Files:</p>
                            </Col>
                            <Col>
                                {
                                    examination_points.map(examination_point => (
                                        <ExaminationPoint key={examination_point.id} examination_point={examination_point}/>
                                    ))
                                }
                            </Col>
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
    examination_point: state.examination_point
});

export default connect(mapStateToProps, {getExaminationPoints})(Dashboard)
