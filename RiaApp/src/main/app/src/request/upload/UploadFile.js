import React, {Component} from 'react';
import {Container} from "reactstrap";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import CloudUploadIcon from "@material-ui/icons/CloudUpload";
import Loading from "../../components/images/Loading";

class UploadFile extends Component {
    constructor(props) {
        super(props);
        this.state = {
            file: "",
            msg: "",
            error: "",
            filename: false,
            image: false,
            errors: {}
        };
    }

    uploadFile = event => {
        event.preventDefault();

        this.setState({error: "", msg: ""});

        if (!this.state.file) {
            this.setState({error: "Please upload a file."});
            return;
        }
        if (this.state.file.size >= 2000000) {
            this.setState({error: "File size exceeds limit of 2MB."});
            return;
        } else {
            this.setState({
                image: true
            });
        }

        let data = new FormData();
        data.append("file", this.state.file);
        data.append("name", this.state.file.name);
        fetch("http://localhost:8080/api/examination/", {
            method: "POST",
            body: data
        })
            .then(response => {
                this.setState({
                    error: "",
                    msg: "Successfully uploaded file",
                    image: false
                });
                window.location.reload();
            })
            .catch(err => {
                this.setState({error: err});
            });
    };

    onFileChange = event => {
        this.setState({
            file: event.target.files[0]
        });
    };

    render() {
        return (
            <Container>
                <br/>
                <Paper>
                    <br/>
                    <input onChange={this.onFileChange} type="file"/>
                    <Button
                        variant="contained"
                        color="default"
                        onClick={this.uploadFile}
                        href={""}>
                        <CloudUploadIcon/>
                    </Button>
                    <Container>
                        <p style={{textAlign: "center", color: "red"}}>
                            {this.state.error}
                        </p>
                        <p style={{textAlign: "center", color: "green"}}>
                            {this.state.msg}
                        </p>
                        {this.state.image ? <Loading/> : null}
                    </Container>
                    <br/>
                </Paper>
            </Container>
        );
    }
}


export default UploadFile;
