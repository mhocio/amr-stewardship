import React from 'react'

// material-ui
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';
import { Paper } from '@mui/material';

// project imports
import PatientsTable from '../components/Tables/PatientsTable';
import ExamTable from '../components/Tables/ExamTable';
import { margin } from '@mui/system';

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));

const PatientsPage = () => {
  return (
    <>
      <DrawerHeader />
      <Grid container spacing={2}>
        <Grid item xs={3.5}>
          <PatientsTable />
        </Grid>
        <Grid item xs={8.5}>
        <ExamTable />
        </Grid>
      </Grid>
    </>

  )
}

export default PatientsPage
