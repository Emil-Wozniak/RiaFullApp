import React, { Component } from "react";
import { Subject } from "@material-ui/icons";
import IconButton from "@material-ui/core/IconButton";
import PropTypes from "prop-types";
import { withStyles } from "@material-ui/core/styles";

const styles = theme => ({
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
    const { classes } = this.props;
    return (
      <div className="landing">
        <div className="container">
          <br />
          <br />
          <IconButton
            variant="contained"
            color="default"
            classes={{ button: "button" }}
            href="/dashboard"
          >
            <Subject className={classes.iconHover} style={{ fontSize: 120 }} />
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
