import React, { Component } from "react";
import { Container, Row, Col } from "reactstrap";
import Paper from "@material-ui/core/Paper";
import IconButton from "@material-ui/core/IconButton";
import { Fingerprint, PersonAdd } from "@material-ui/icons/";
import PropTypes from "prop-types";
import { connect } from "react-redux";
import classnames from "classnames";
import { login } from "../../../actions/securityActions";

class Login extends Component {
  constructor() {
    super();
    this.state = {
      password: "",
      username: "",
      errors: {}
    };
    this.onChange = this.onChange.bind(this);
    this.onSubmit = this.onSubmit.bind(this);
  }

  onChange(e) {
    this.setState({ [e.target.name]: e.target.value });
  }

  onSubmit(e) {
    e.preventDefault();
    const LoginRequest = {
      password: this.state.password,
      username: this.state.username
    };
    this.props.login(LoginRequest);
  }

  componentWillReceiveProps(nextProps) {
    if (nextProps.security.validToken) {
      this.props.history.push("/dashboard");
    }
    if (nextProps.errors) {
      this.setState({ errors: nextProps.errors });
    }
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
              <h4 className="text-center">Login</h4>
              <Container>
                <form onSubmit={this.onSubmit}>
                  <Col>
                    <div className="form-group">
                      <input
                        type="text"
                        className={classnames("form-control form-control-lg", {
                          "is-invalid": errors.username
                        })}
                        placeholder="Email Address (username)"
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
                    <IconButton type="submit">
                      <Fingerprint style={{ width: "50px", height: "50px" }} />
                    </IconButton>
                  </Col>
                </form>
                <IconButton href="/register">
                  <PersonAdd style={{ width: "50px", height: "50px" }} />
                </IconButton>
                <br />
                <br />
              </Container>
            </Col>
          </Row>
        </Paper>
      </Container>
    );
  }
}

Login.propTypes = {
  login: PropTypes.func.isRequired,
  errors: PropTypes.object.isRequired,
  security: PropTypes.object.isRequired
};

const mapStateToProps = state => ({
  security: state.security,
  errors: state.errors
});

export default connect(
  mapStateToProps,
  { login }
)(Login);
