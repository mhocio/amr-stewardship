import React, { useState, useEffect } from 'react'
import axios from 'axios';
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
    axios.all([
      await axios.get(`${BASE_URL}/patient`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Accept': 'application/json, text/plain',
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
          // 'Authorization': 'Bearer ' + authToken
        }
      }),
      await axios.get(`${BASE_URL}/antibiogram`, {
        method: 'GET',
        mode: 'cors',
        headers: {
          'Accept': 'application/json, text/plain',
          'Content-Type': 'application/json',
          'Access-Control-Allow-Origin': '*'
          // 'Authorization': 'Bearer ' + authToken
        }
      })
    ])
      .then(axios.spread((res1, res2) => {
        setPatients(res1.data);
        setAntibiograms(res2.data);
      }))
      .catch((err) => {
        setErrorFlag(true);
        console.log(`Couldn't fetch tables data: ${err}`);
      })
      .finally(() => {
        setLoading(false);
      });

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
