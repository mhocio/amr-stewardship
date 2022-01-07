import React from 'react';

//material-ui
import { AppBar, Typography, Toolbar, Grid, IconButton } from '@mui/material';
import { styled } from '@mui/material/styles';
import LocalHospitalIcon from '@mui/icons-material/LocalHospital';
import MenuIcon from '@mui/icons-material/Menu';

// project imports
import Account from './AccountChip';

const drawerWidth = 240;

const MyAppBar = styled(AppBar, {
  shouldForwardProp: (prop) => prop !== 'open',
})(({ theme, open }) => ({
  transition: theme.transitions.create(['margin', 'width'], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen,
  }),
  ...(open && {
    width: `calc(100% - ${drawerWidth}px)`,
    marginLeft: `${drawerWidth}px`,
    transition: theme.transitions.create(['margin', 'width'], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen,
    }),
  }),
}));


export default function Header({ toggleDrawer, isOpen, isAuth }) {
  return (
    <MyAppBar position="fixed" open={isOpen}>
      <Toolbar>
        {isAuth &&
          <IconButton
            color="inherit"
            aria-label="toggle drawer"
            onClick={toggleDrawer}
            edge="start"
            sx={{ mr: 2, ...(isOpen && { display: 'none' }) }}
          >
            <MenuIcon />
          </IconButton>
        }
        <Grid container spacing={2} direction="row" justifyContent="space-between" alignItems="center">
          <Grid container item direction="row" alignItems="center" wrap="nowrap">
            {!isAuth &&
              <Grid item>
                <LocalHospitalIcon fontSize="large" />
              </Grid>
            }
            <Grid item>
              <Typography variant="h6" noWrap component="div">
                System do zarządzania antybiotykoterapią
              </Typography>
            </Grid>
          </Grid>
        </Grid>
        <Grid item>
          {isAuth &&
            <Account person='Jan Kowalski' />
          }
        </Grid>
      </Toolbar>
    </MyAppBar>
  )
}