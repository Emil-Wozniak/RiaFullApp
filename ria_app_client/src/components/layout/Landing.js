import React, { Component } from "react";
import IconButton from "@material-ui/core/IconButton";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

const styles = theme => ({
  typography: {
    useNextVariants: true,
  },
  root: {
    display: "flex",
    justifyContent: "center",
    alignItems: "flex-end"
  },
  icon: {
    margin: theme.spacing.unit * 2
  },
  iconHover: {
    margin: theme.spacing.unit * 2
  }
});

class Landing extends Component {
  render() {
    return (
      <div className="landing">
        <div className="container">
          <br />
          <br />
          <IconButton
            variant="contained"
            color="default"
            href="/dashboard"
          >
          <img src={require('./images/pe-trilux.jpeg')} style={{width: '100px', height: '100px'}} alt="pe-trilux"/>
          </IconButton>
        </div>
      </div>
    );
  }
}

Landing.propTypes = {
  classes: PropTypes.object.isRequired
};

export default withStyles(styles)(Landing);
