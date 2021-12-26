import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
// import { options as initialOptions } from "./options";

const initialOptions = [
  { label: 'Amikacyna', value: 'amikacyna' },
  { label: 'Ampicylina', value: 'ampicylina' },
  { label: 'Cefepim', value: 'cefepim' },
  { label: 'Cefuroksym', value: 'cefuroksym' },
  { label: 'Ciprofloksacyna', value: 'ciprofloksacyna' },
  { label: 'Gentamycyna', value: 'gentamycyna' }
];

export default function AntibioticSelect() {
  return (
    <Autocomplete
    disablePortal
    id="antibiotic-select"
    options={initialOptions}
    sx={{ width: '35vh' }}
    renderInput={(params) => <TextField {...params} label="Antybiotyk" />}
  />
  );
}

