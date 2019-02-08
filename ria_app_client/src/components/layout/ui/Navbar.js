import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import LongMenu from "./LongMenu";
import RenderPropsMenu from "./RenderPropsMenu";
const NavBar = () => {
  return (
    <AppBar position="static">
      <Toolbar>
        <Grid
          justify="space-between" // Add it here :)
          container
          spacing={24}
        >
          <LongMenu />
          <Typography variant="title" color="inherit">
            RiaApp
          </Typography>
          <RenderPropsMenu />
        </Grid>
      </Toolbar>
    </AppBar>
  );
};
export default NavBar;
