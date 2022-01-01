import React from 'react'

// material-ui
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import { Paper } from '@mui/material';

// project imports
import FratMenu from '../components/FratMenu';
import FratTable from '../components/Tables/FratTable';

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));


const FratPage = () => {
  return (
    <>
      <DrawerHeader />
      <Grid
        container
        direction="column"
        justifyContent=""
        spacing={3}
      >
        <Grid item>
          <Paper sx={{ padding: '20px'}}>
          <FratMenu />
          </Paper>
        </Grid>
        <Grid item>
          <FratTable />
        </Grid>
      </Grid>
    </>

  )
}

export default FratPage
