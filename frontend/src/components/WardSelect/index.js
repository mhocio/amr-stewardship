import React, { useState, useEffect } from 'react';

// material-ui
import { Paper, TextField, Autocomplete } from '@mui/material';

// project imports
import BASE_URL from '../../constants/BASE_URL';
import authHeader from '../../services/auth-header';

// third-party
import axios from 'axios';

export default function WardSelect({ handleSetWard }) {

  const [ward, setWard] = useState([]);
  const [errorFlag, setErrorFlag] = useState(false);

  useEffect(() => {
    getAllWards();
  }, [])

  const getAllWards = () => {
    axios.get(`${BASE_URL}/ward`, {
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
      setWard(res.data);
    })
    .catch((err) => {
      setErrorFlag(true);
    })
  }

  return (
    <Paper sx={{ padding: '20px' }}>
      <Autocomplete
        disablePortal
        id="ward-select"
        getOptionLabel={option => option.name}
        getOptionValue={option => option.materialId}
        options={ward}
        onChange={(event, newValue) => {
          handleSetWard(newValue?.name);
        }}
        renderInput={(params) => <TextField {...params} label="Oddział"/>}
      />
    </Paper>
  );
}

