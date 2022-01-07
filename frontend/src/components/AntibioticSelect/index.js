import * as React from 'react';

// material-ui
import { Paper, TextField, Autocomplete } from '@mui/material';

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
    <Paper sx={{ padding: '20px' }}>
      <Autocomplete
        disablePortal
        id="antibiotic-select"
        options={initialOptions}
        renderInput={(params) => <TextField {...params} label="Antybiotyk" />}
      />
    </Paper>
  );
}

