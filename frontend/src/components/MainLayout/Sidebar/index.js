import React from 'react';

// material-ui
import { styled, useTheme } from '@mui/material/styles';
import ChevronLeftIcon from '@mui/icons-material/ChevronLeft';
import ChevronRightIcon from '@mui/icons-material/ChevronRight';
import ListItem from '@mui/material/ListItem';
import ListItemIcon from '@mui/material/ListItemIcon';
import ListItemText from '@mui/material/ListItemText';
import PersonIcon from '@mui/icons-material/Person';
import BarChartIcon from '@mui/icons-material/BarChart';
import NumbersIcon from '@mui/icons-material/Numbers';
import SettingsIcon from '@mui/icons-material/Settings';
import Divider from '@mui/material/Divider';
import List from '@mui/material/List';
import Drawer from '@mui/material/Drawer';
import IconButton from '@mui/material/IconButton';
import { useNavigate } from 'react-router-dom';

const drawerWidth = 240;


const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));

export default function Sidebar({ toggleDrawer, isOpen }) {
  const theme = useTheme();

  const navigate = useNavigate();

  return (
    <Drawer
      sx={{
        width: drawerWidth,
        flexShrink: 0,
        '& .MuiDrawer-paper': {
          width: drawerWidth,
          boxSizing: 'border-box',
        },
      }}
      variant="persistent"
      anchor="left"
      open={isOpen}
    >
      <DrawerHeader>
        <IconButton
          edge="start"
          onClick={toggleDrawer}>
          {theme.direction === 'ltr' ? <ChevronLeftIcon /> : <ChevronRightIcon />}
        </IconButton>
      </DrawerHeader>
      <Divider />
      <List>
        <ListItem button key='Pacjenci' onClick={() => navigate('/page/patients')}>
          <ListItemIcon>
            <PersonIcon />
          </ListItemIcon>
          <ListItemText primary='Pacjenci' />
        </ListItem>
        <ListItem button key='Trendy' onClick={() => navigate('/page/trends')}>
          <ListItemIcon>
            <BarChartIcon />
          </ListItemIcon>
          <ListItemText primary='Trendy' />
        </ListItem>
        <ListItem button key='FRAT' onClick={() => navigate('/page/frat')}>
          <ListItemIcon>
            <NumbersIcon />
          </ListItemIcon>
          <ListItemText primary='FRAT' />
        </ListItem>
        <ListItem button key='Ustawienia' onClick={() => navigate('/page/settings')}>
          <ListItemIcon>
            <SettingsIcon />
          </ListItemIcon>
          <ListItemText primary='Ustawienia' />
        </ListItem>
      </List>
    </Drawer>

  )
}