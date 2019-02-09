import React from "react";
import AppBar from "@material-ui/core/AppBar";
import Toolbar from "@material-ui/core/Toolbar";
import Grid from "@material-ui/core/Grid";
import Typography from "@material-ui/core/Typography";
import LongMenu from "./LongMenu";
import RenderPropsMenu from "./RenderPropsMenu";
import { createMuiTheme, MuiThemeProvider } from '@material-ui/core/styles';
import createPalette from '@material-ui/core/styles/createPalette';
import grey from '@material-ui/core/colors/green';
import red from '@material-ui/core/colors/red';
import green from '@material-ui/core/colors/green';
import yellow from '@material-ui/core/colors/yellow';
import purple from '@material-ui/core/colors/purple';

const theme = createMuiTheme({
  palette: createPalette({
    type: 'dark',
    primary: purple,
    secondary: green,
    accent: grey,
    error: red,
    success: green,
    inProgress: yellow
  }),
  typography: {
    // Use the system font instead of the default Roboto font.
    fontFamily: [
      '-apple-system',
      'BlinkMacSystemFont',
      '"Segoe UI"',
      'Roboto',
      '"Helvetica Neue"',
      'Arial',
      'sans-serif',
      '"Apple Color Emoji"',
      '"Segoe UI Emoji"',
      '"Segoe UI Symbol"',
    ].join(','),
  },
  overrides: {
    MuiButton: { // Name of the component ⚛️ / style sheet
      text: { // Name of the rule
        color: 'white', // Some CSS
      },
    },
  },
})

const NavBar = (props) => {
  return (
    <MuiThemeProvider theme={theme}>
    <AppBar position="static" color="theme">
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
    </MuiThemeProvider>
  );
};

export default NavBar;