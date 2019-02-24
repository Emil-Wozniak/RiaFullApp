import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Grid from "@material-ui/core/Grid";
import RenderPropsMenu from "./RenderPropsMenu";
import { createMuiTheme, MuiThemeProvider } from "@material-ui/core/styles";

const theme = createMuiTheme({
  typography: {
    useNextVariants: true,
  },
  palette:{
    primary: {
      main: '#fafafa',
    }
  }
});

const NavBar = () => {
  return (
    <MuiThemeProvider theme={theme}>
      <AppBar position="static" color="primary">
        <Toolbar>
          <Grid justify="space-between" container spacing={24}>
            <h4>
            <img src={require('../images/analysis.png')} style={{width: '50px', height: '50px'}} alt="RiaApp"/> RiaApp
            </h4>
            <RenderPropsMenu />
          </Grid>
        </Toolbar>
      </AppBar>
    </MuiThemeProvider>
  );
};

export default NavBar;
