import React, {Component} from 'react';
import PropTypes from "prop-types";
import {Col, Row} from "reactstrap";
import {Link} from "react-router-dom";
import IconButton from "@material-ui/core/IconButton";
import FolderIcon from "@material-ui/icons/Folder";
import {connect} from "react-redux";

class ExaminationPoint extends Component {
    render() {
        const {examination_point} = this.props;

        const filename = (examination_point.probeNumber === 25) ? examination_point.identifier : "";
        const pattern = examination_point.probeNumber === 25 ? examination_point.pattern : "";
        const folder = examination_point.probeNumber === 25 ? <IconButton><FolderIcon/></IconButton> : "";

        return (
            <React.Fragment>
                <Row>
                    <Col>{folder}</Col>
                    <Col><Link to={`/file/${examination_point.identifier}`}>{filename}</Link></Col>
                    <Col>{pattern}</Col>
                </Row>
            </React.Fragment>
        );
    }
}

ExaminationPoint.propTyes = {
    classes: PropTypes.object.isRequired
};

export default connect(null, null)(ExaminationPoint);
