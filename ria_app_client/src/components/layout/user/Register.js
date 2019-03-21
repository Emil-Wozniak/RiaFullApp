import React, { Component } from "react";
import { createNewUser } from "../../../actions/securityActions";
import { Container, Row, Col } from "reactstrap";
import Paper from "@material-ui/core/Paper";
import IconButton from "@material-ui/core/IconButton";
import { Person, PersonAdd } from "@material-ui/icons/";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import classnames from "classnames";

class Register extends Component {
  constructor() {
    super();

    this.state = {
      username: "",
      fullName: "",
      password: "",
      confirmPassword: "",
      errors: {}
    };
    this.onChange = this.onChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.errors) {
      this.setState({ errors: nextProps.errors });
    }
  }

  onChange(e) {
    this.setState({ [e.target.name]: e.target.value });
  }
  onSubmit(e) {
    e.preventDefault();

    const newUser = {
      username: this.state.username,
      fullName: this.state.fullName,
      password: this.state.password,
      confirmPassword: this.state.confirmPassword
    };

    this.props.createNewUser(newUser, this.props.history);
  }

  render() {
    const { errors } = this.state;
    return (
      <Container>
        <br />
        <Paper>
          <Row>
            <Col>
              <br />
              <h4 className="text-center">Sign Up</h4>
              <h6 className="lead text-center">Create your Account</h6>
              <Container>
                <form onSubmit={this.onSubmit}>
                    <div className="form-group">
                      <input
                        type="text"
                        className={classnames("form-control form-control-lg", {
                          "is-invalid": errors.fullName
                        })}
                        placeholder="Full name"
                        name="fullName"
                        value={this.state.fullName}
                        onChange={this.onChange}
                      />
                      {errors.fullName && (
                        <div className="invalid-feedback">
                          {errors.fullName}
                        </div>
                      )}
                    </div>
                    <div className="form-group">
                      <input
                        type="text"
                        className={classnames("form-control form-control-lg", {
                          "is-invalid": errors.name
                        })}
                        placeholder="Email Address (Username)"
                        name="username"
                        value={this.state.username}
                        onChange={this.onChange}
                      />
                      {errors.username && (
                        <div className="invalid-feedback">
                          {errors.username}
                        </div>
                      )}
                    </div>
                    <div className="form-group">
                      <input
                        type="password"
                        className={classnames("form-control form-control-lg", {
                          "is-invalid": errors.password
                        })}
                        placeholder="Password"
                        name="password"
                        value={this.state.password}
                        onChange={this.onChange}
                      />
                      {errors.password && (
                        <div className="invalid-feedback">
                          {errors.password}
                        </div>
                      )}
                    </div>
                    <div className="form-group">
                      <input
                        type="password"
                        className={classnames("form-control form-control-lg", {
                          "is-invalid": errors.confirmPassword
                        })}
                        placeholder="Confirm Password"
                        name="confirmPassword"
                        value={this.state.confirmPassword}
                        onChange={this.onChange}
                      />
                      {errors.confirmPassword && (
                        <div className="invalid-feedback">
                          {errors.confirmPassword}
                        </div>
                      )}
                    </div>
                    <IconButton type="submit" href="/login">
                      <PersonAdd style={{ width: "50px", height: "50px" }} />
                    </IconButton>
                    <IconButton href="/login">
                      <Person style={{ width: "50px", height: "50px" }} />
                    </IconButton>
                    <br/>
                    <br/>
                </form>
              </Container>
            </Col>
          </Row>
        </Paper>
      </Container>
    );
  }
}

Register.propTypes = {
  createNewUser: PropTypes.func.isRequired,
  errors: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
  errors: state.errors
});

export default connect(
  mapStateToProps,
  { createNewUser }
)(Register);
