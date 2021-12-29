import * as React from 'react';
import AppBar from '@mui/material/AppBar';
import Box from '@mui/material/Box';
import Toolbar from '@mui/material/Toolbar';
import Typography from '@mui/material/Typography';

import IconButton from '@mui/material/IconButton';
import MenuIcon from '@mui/icons-material/Menu';
import { Paper, TextField, Button, FormControlLabel, Checkbox } from '@mui/material';
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import AlternateEmailIcon from '@mui/icons-material/AlternateEmail';
import VpnKeyIcon from '@mui/icons-material/VpnKey';

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));


export default function LoginTab(props) {
  return (
    <>
      <DrawerHeader />
      <Grid container alignItems="center" justifyContent="center">
        <Paper style={{
          position: 'absolute', left: '50%', top: '50%',
          transform: 'translate(-50%, -50%)',
          padding: '30px'
        }}>
          <Grid item>
            <Typography variant="h6" style={{ paddingBottom: "10px" }}>Logowanie</Typography>
          </Grid>
          <Grid container spacing={2} alignItems="center">
            <Grid item>
              <AlternateEmailIcon fontSize="medium" />
            </Grid>
            <Grid item>
              <TextField id="username" label="Email" type="email" fullWidth autoFocus required />
            </Grid>
          </Grid>
          <Grid container spacing={2} alignItems="center">
            <Grid item>
              <VpnKeyIcon fontSize="medium" />
            </Grid>
            <Grid item>
              <TextField id="password" label="Hasło" type="password" fullWidth required />
            </Grid>
          </Grid>
          <Grid container direction="column" alignItems="center" justifyContent="center" style={{ marginTop: '25px' }}>
            <Grid item>
              <Button variant="contained" color="primary" style={{ textTransform: "none", width: "30vh" }}>Zaloguj</Button>
            </Grid>
            <Grid item>
              <Button disableFocusRipple disableRipple style={{ textTransform: "none" }} variant="text" color="primary">Nie pamiętam hasła</Button>
            </Grid>
          </Grid>
        </Paper>
      </Grid>
    </>
  );
}