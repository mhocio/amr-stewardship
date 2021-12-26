import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
// import { options as initialOptions } from "./options";

const initialOptions = [
  { label: '2016', value: '2016' },
  { label: '2017', value: '2017' },
  { label: '2018', value: '2018' },
  { label: '2019', value: '2019' },
  { label: '2020', value: '2020' },
  { label: '2021', value: '2021' }
];

export default function YearSelect() {
  return (
    <Autocomplete
    disablePortal
    id="year-select"
    options={initialOptions}
    sx={{ width: '35vh' }}
    renderInput={(params) => <TextField {...params} label="Rok" />}
  />
  );
}

