import * as React from 'react';

// material-ui
import { styled } from '@mui/material/styles';
import { Box, LinearProgress }from '@mui/material';
import CssBaseline from '@mui/material/CssBaseline';

// project imports
import Sidebar from './Sidebar';
import Header from './Header';

const drawerWidth = 240;

const Main = styled('main', { shouldForwardProp: (prop) => prop !== 'open' })(
    ({ theme, open }) => ({
      flexGrow: 1,
      padding: theme.spacing(3),
      transition: theme.transitions.create('margin', {
        easing: theme.transitions.easing.sharp,
        duration: theme.transitions.duration.leavingScreen,
      }),
      marginLeft: `-${drawerWidth}px`,
      ...(open && {
        transition: theme.transitions.create('margin', {
          easing: theme.transitions.easing.easeOut,
          duration: theme.transitions.duration.enteringScreen,
        }),
        marginLeft: 0,
      }),
    }),
  );

export default function MainLayout({ children }) {
  const [open, setOpen] = React.useState(false);

  const toggleDrawer = () => {
    setOpen(value => !value)
  };

  return (
    <Box sx={{ display: 'flex' }}>
      <CssBaseline />
        <Header toggleDrawer={toggleDrawer} isOpen={open}/>
        <Sidebar toggleDrawer={toggleDrawer} isOpen={open}/>
        <Main open={open}>
        {children}
        </Main>
    </Box>
  );
}
