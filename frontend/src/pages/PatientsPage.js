import React, { useState, useEffect } from 'react'

// material-ui
import { styled } from '@mui/material/styles';
import { Paper, Grid } from '@mui/material';

// project imports
import PatientsTable from '../components/Tables/PatientsTable';
import ExamTable from '../components/Tables/ExamTable';
import { margin } from '@mui/system';
import BASE_URL from '../constants/BASE_URL';

const DrawerHeader = styled('div')(({ theme }) => ({
  display: 'flex',
  alignItems: 'center',
  padding: theme.spacing(0, 1),
  // necessary for content to be below app bar
  ...theme.mixins.toolbar,
  justifyContent: 'flex-end',
}));

const PatientsPage = () => {

  const [patients, setPatients] = useState([]);
  const [antibiograms, setAntibiograms] = useState([]);
  const [errorFlag, setErrorFlag] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getTablesData();
  }, [])

  const getTablesData = async () => {
    setLoading(true);
    Promise.all([
      await fetch(`${BASE_URL}/patients`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Accept': 'application/json, text/plain',
          'Content-Type': 'application/json'
          // 'Authorization': 'Bearer ' + authToken
        }
      }),
      await fetch(`${BASE_URL}/antibiograms`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Accept': 'application/json, text/plain',
          'Content-Type': 'application/json'
          // 'Authorization': 'Bearer ' + authToken
        }
      })
    ])
      .then(([res1, res2]) => Promise.all([res1.json(), res2.json()]))
      .then(([data1, data2]) => {
        setPatients(data1);
        setAntibiograms(data2);
      })
      .catch((err) => {
        setErrorFlag(true);
        console.log(`Couldn't fetch tables data: ${err}`);
      })
      .finally(() => {
        setLoading(false);
      })

  }

  return (
    <>
      <DrawerHeader />
      <Grid container spacing={2}>
        <Grid item xs={3.5}>
          <PatientsTable data={patients} loading={loading} />
        </Grid>
        <Grid item xs={8.5}>
          <ExamTable data={antibiograms} loading={loading} />
        </Grid>
      </Grid>
    </>

  )
}

export default PatientsPage
