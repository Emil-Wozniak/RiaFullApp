import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Grid from "@material-ui/core/Grid";
import RenderPropsMenu from "./RenderPropsMenu";
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles";
import createPalette from "@material-ui/core/styles/createPalette";
import {grey, red, green, yellow, purple} from "@material-ui/core/colors/";
import {Polymer} from "@material-ui/icons";

const theme = createMuiTheme({
  palette: createPalette({
    primary: purple,
    secondary: green,
    accent: grey,
    error: red,
    success: green,
    inProgress: yellow
  }),
  typography: {
    fontFamily: [
      "-apple-system",
      "BlinkMacSystemFont",
      '"Segoe UI"',
      "Roboto",
      '"Helvetica Neue"',
      "Arial",
      "sans-serif",
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"'
    ].join(",")
  },
});

const NavBar = () => {
  return (
    <MuiThemeProvider theme={theme}>
      <AppBar position="static" color="theme">
        <Toolbar>
          <Grid
            justify="space-between"
            container
            spacing={24}
          >
            <h4><Polymer/>{" "}RiaApp</h4>
            <RenderPropsMenu />
          </Grid>
        </Toolbar>
      </AppBar>
    </MuiThemeProvider>
  );
};

export default NavBar;
