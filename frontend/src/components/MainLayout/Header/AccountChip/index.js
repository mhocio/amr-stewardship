import React from 'react'

// material-ui
import { ClickAwayListener, Popper, Paper, Fade, Chip, List, ListItemButton, ListItemText, ListItemIcon, ListSubheader, Avatar, Typography } from '@mui/material';
import LogoutIcon from '@mui/icons-material/Logout';

export default function AccountChip({ person }) {

  var initials = person.split(" ").map((x) => x[0]).join('');

  const [anchorEl, setAnchorEl] = React.useState(null);
  const [open, setOpen] = React.useState(false);
  const [placement, setPlacement] = React.useState();

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
    console.log("Logging out");
  };

  return (
    <>
      <Chip avatar={<Avatar>{initials}</Avatar>} ref={anchorEl} label={person} color="secondary" onClick={handleClick('bottom-end')} />
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
                      Dzień dobry, Jan
                    </ListSubheader>
                  }
                >
                  <ListItemButton>
                    <ListItemIcon>
                      <LogoutIcon />
                    </ListItemIcon>
                    <ListItemText primary={<Typography variant="body2">Wyloguj</Typography>} />
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
