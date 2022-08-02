import React from "react";

// material-ui
import {
  AppBar,
  Typography,
  Toolbar,
  Grid,
  IconButton,
  LinearProgress
} from "@mui/material";
import { styled } from "@mui/material/styles";
import LocalHospitalIcon from "@mui/icons-material/LocalHospital";
import MenuIcon from "@mui/icons-material/Menu";

// project imports
import Account from "./AccountChip";
import { useAuth } from "../../../context/auth";
import { useLoading } from "../../../loading/loading-context";

const drawerWidth = 240;

const MyAppBar = styled(AppBar, {
  shouldForwardProp: (prop) => prop !== "open"
})(({ theme, open }) => ({
  transition: theme.transitions.create(["margin", "width"], {
    easing: theme.transitions.easing.sharp,
    duration: theme.transitions.duration.leavingScreen
  }),
  ...(open && {
    width: `calc(100% - ${drawerWidth}px)`,
    marginLeft: `${drawerWidth}px`,
    transition: theme.transitions.create(["margin", "width"], {
      easing: theme.transitions.easing.easeOut,
      duration: theme.transitions.duration.enteringScreen
    })
  })
}));

export default function Header({ toggleDrawer, isOpen }) {
  const { loading, setLoading } = useLoading();
  const { user } = useAuth();

  return (
    <>
      <MyAppBar position="fixed" open={isOpen}>
        <Toolbar>
          {user ? (
            <IconButton
              color="inherit"
              aria-label="toggle drawer"
              onClick={toggleDrawer}
              edge="start"
              sx={{ mr: 2, ...(isOpen && { display: "none" }) }}
            >
              <MenuIcon />
            </IconButton>
          ) : (
            <LocalHospitalIcon sx={{ mr: 3 }} />
          )}
          <Grid
            container
            direction="row"
            justifyContent="space-between"
            alignItems="center"
          >
            <Grid
              container
              item
              direction="row"
              alignItems="center"
              wrap="nowrap"
            >
              <Typography variant="h6" noWrap component="div">
                System do zarządzania antybiotykoterapią (v 1.0)
              </Typography>
            </Grid>
          </Grid>
          <Grid item>{user && <Account person={user.username} />}</Grid>
        </Toolbar>
        {loading && <LinearProgress />}
      </MyAppBar>
    </>
  );
}
