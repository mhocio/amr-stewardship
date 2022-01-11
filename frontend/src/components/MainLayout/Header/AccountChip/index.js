import React from 'react';
import { useNavigate } from 'react-router-dom';

// material-ui
import { ClickAwayListener, Popper, Paper, Fade, Chip, List, ListItemButton, ListItemText, ListItemIcon, ListSubheader, Avatar, Typography } from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';


import { useAuth } from '../../../../context/auth'
import BASE_URL from '../../../../constants/BASE_URL';

// third-party
import axios from 'axios';

export default function AccountChip({ person }) {

  const [anchorEl, setAnchorEl] = React.useState(null);
  const [open, setOpen] = React.useState(false);
  const [placement, setPlacement] = React.useState();

  const navigate = useNavigate();
  const { user } = useAuth();

  const handleClick = (newPlacement) => (event) => {
    setAnchorEl(event.currentTarget);
    setOpen((prev) => !prev);
    setPlacement(newPlacement);
  };


  const handleClose = (event) => {
    if (anchorEl.current && anchorEl.current.contains(event.target)) {
      return;
    }
    setOpen(false);
  };

  const handleLogout = () => {
    axios.post(`${BASE_URL}/auth/logout`, {
      "refreshToken": user.refreshToken,
      "username": user.username
    }
    )
      .then(res => {
        console.log(res);
        localStorage.removeItem("user");
        window.location.reload();
      })
  };

  return (
    <>
      <Chip avatar={<Avatar>{person.split(" ").map((x) => x[0]).join('')}</Avatar>} ref={anchorEl} label={person} color="secondary" onClick={handleClick('bottom-end')} />
      <Popper open={open} anchorEl={anchorEl} placement={placement} transition popperOptions={{
        modifiers: [
          {
            name: 'offset',
            options: {
              offset: [0, 15]
            }
          }
        ]
      }}>
        {({ TransitionProps }) => (
          <Fade {...TransitionProps} timeout={350}>
            <Paper sx={{ boxShadow: 3 }}>
              <ClickAwayListener onClickAway={handleClose}>
                <List
                  sx={{ width: '100%', maxWidth: 400, bgcolor: 'background.paper' }}
                  component="nav"
                  aria-labelledby="nested-list-subheader"
                  subheader={
                    <ListSubheader component="div" id="nested-list-subheader">
                      Dzień dobry, {person}
                    </ListSubheader>
                  }
                >
                  <ListItemButton>
                    <ListItemIcon>
                      <LogoutIcon />
                    </ListItemIcon>
                    <ListItemText primary={<Typography variant="body2">Wyloguj</Typography>} onClick={() => handleLogout()} />
                  </ListItemButton>
                </List>
              </ClickAwayListener>
            </Paper>
          </Fade>
        )}
      </Popper>
    </>
  )
}
