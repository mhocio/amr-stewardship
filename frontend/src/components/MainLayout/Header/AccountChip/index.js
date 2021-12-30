import React from 'react'

// material-ui
import { Avatar, Typography } from '@material-ui/core'
import { Popper, Paper, Fade, Chip } from '@mui/material';
import { styled } from '@mui/material/styles';
import ClickAwayListener from '@mui/material/ClickAwayListener';
import { Grid } from '@mui/material';
import { Divider } from '@mui/material';
import { List, ListItemButton, ListItemText, ListItemIcon } from '@mui/material';
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
            <Paper>
              <ClickAwayListener onClickAway={handleClose}>
                <Grid container direction="column" spacing={0}>
                  <Grid item>
                    <Typography variant="h4">Good Morning,</Typography>
                    <Typography component="span" variant="h4">
                      John
                    </Typography>
                  </Grid>
                  <Grid item>
                    <Typography variant="subtitle2">Project Admin</Typography>
                  </Grid>
                </Grid>
              </ClickAwayListener>
            </Paper>
          </Fade>
        )}
      </Popper>
    </>
  )
}
