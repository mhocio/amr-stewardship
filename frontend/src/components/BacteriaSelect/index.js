import React, { useState, useEffect } from 'react';

// material-ui
import { Paper, TextField, Autocomplete } from '@mui/material';

// project imports
import BASE_URL from '../../constants/BASE_URL';
import authHeader from '../../services/auth-header';

// third-party
import axios from 'axios';


const initialOptions = [
  { label: 'Amikacyna', value: 'amikacyna' },
  { label: 'Ampicylina', value: 'ampicylina' },
  { label: 'Cefepim', value: 'cefepim' },
  { label: 'Cefuroksym', value: 'cefuroksym' },
  { label: 'Ciprofloksacyna', value: 'ciprofloksacyna' },
  { label: 'Gentamycyna', value: 'gentamycyna' }
];


export default function BacteriaSelect({ handleSusceptibilityChart }) {

  const [bacteria, setBacteria] = useState([]);
  const [errorFlag, setErrorFlag] = useState(false);

  useEffect(() => {
    getAllBacteria();
  }, [])

  const getAllBacteria = () => {
    axios.get(`${BASE_URL}/bacteria`, {
      method: 'GET',
      mode: 'cors',
      headers: {
        'Accept': 'application/json, text/plain',
        'Content-Type': 'application/json',
        'Access-Control-Allow-Origin': '*',
        ...authHeader()
      }
    })
    .then((res) => {
      console.log(res.data);
      setBacteria(res.data);
    })
    .catch((err) => {
      setErrorFlag(true);
    })
  }

  return (
    <Paper sx={{ padding: '20px' }}>
      <Autocomplete
        disablePortal
        id="bacteria-select"
        getOptionLabel={option => option.name}
        getOptionValue={option => option.bacteriaId}
        options={bacteria}
        onChange={(event, newValue) => {
          handleSusceptibilityChart(newValue?.name);
        }}
        renderInput={(params) => <TextField {...params} label="Drobnoustrój"/>}
      />
    </Paper>
  );
}

