import React from "react";
import Button from "@material-ui/core/Button";
import Menu from "@material-ui/core/Menu";
import MenuItem from "@material-ui/core/MenuItem";
import toRenderProps from "recompose/toRenderProps";
import withState from "recompose/withState";
import { logout } from "../../../actions/securityActions";
import store from "../../../store";

const WithState = toRenderProps(withState("anchorEl", "updateAnchorEl", null));

function RenderPropsMenu() {
  return (
    <WithState>
      {({ anchorEl, updateAnchorEl }) => {
        const open = Boolean(anchorEl);
        const handleClose = () => {
          updateAnchorEl(null);
        };
        const handleRegister = () => {
          window.location.href = "/register";
        };

        const handleLogout = () => {
          store.dispatch(logout());
          window.location.href = "/";
        };

        return (
          <React.Fragment>
            <Button
              aria-owns={open ? "render-props-menu" : undefined}
              aria-haspopup="true"
              onClick={event => {
                updateAnchorEl(event.currentTarget);
              }}
            >
              User
            </Button>
            <Menu
              id="render-props-menu"
              anchorEl={anchorEl}
              open={open}
              onClose={handleClose}
            >
              <MenuItem onClick={handleRegister}>Register</MenuItem>
              <MenuItem onClick={handleLogout}>Logout</MenuItem>
            </Menu>
          </React.Fragment>
        );
      }}
    </WithState>
  );
}

export default RenderPropsMenu;
