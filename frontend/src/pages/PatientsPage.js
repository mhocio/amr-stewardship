import React from 'react'

// material-ui
import { styled } from '@mui/material/styles';
import Grid from '@mui/material/Grid';

// project imports
import PatientsTable from '../components/PatientsTable';
import ExamTable from '../components/ExamTable';

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
        <Grid item xs={3}>
        <PatientsTable />
        </Grid>
        <Grid item xs={9}>
        <ExamTable />
        </Grid>
      </Grid>
    </>

  )
}

export default PatientsPage
