import React, {Component} from 'react';
import PropTypes from "prop-types";
import {Link} from "react-router-dom";
import IconButton from "@material-ui/core/IconButton";
import FolderIcon from "@material-ui/icons/Folder";
import {connect} from "react-redux";
import Col from "reactstrap/es/Col";

class ExaminationPoint extends Component {
    render() {
        const {examination_point} = this.props;
        const folder = examination_point.probeNumber === 25 ?
            <td><IconButton href={""}><Link
                to={`/file/${examination_point.identifier}`}><FolderIcon/></Link></IconButton></td> :
            "";
        const filename = (examination_point.probeNumber === 25) ?
            <td><Link to={`/file/${examination_point.identifier}`}>
                <Col className={"Cell-Text"}>{examination_point.identifier}</Col>
            </Link></td> : "";
        const pattern = examination_point.probeNumber === 25 ?
            <td>
                <Col className={"Cell-Text"}>{examination_point.pattern}</Col>
            </td> : "";

        return (
            <React.Fragment>
                <tbody>
                <tr>
                    {folder}
                    {filename}
                    {pattern}
                </tr>
                </tbody>
            </React.Fragment>
        );
    }
}

ExaminationPoint.propTyes = {
    classes: PropTypes.object.isRequired
};

export default connect(null, null)(ExaminationPoint);
