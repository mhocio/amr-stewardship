import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
import { Paper } from '@mui/material';
// import { options as initialOptions } from "./options";

const initialOptions = [
  { label: 'Karbapenemy', value: 'karbapenemy' },
  { label: 'Glikopeptydy', value: 'glikopeptydy' },
  { label: 'Kolistyna', value: 'kolistyna' },
  { label: 'Cefalosporyny', value: 'cefalosporyny' },
  { label: 'Aminoglikozydy', value: 'aminoglikozydy' },
  { label: 'Penicyliny', value: 'penicyliny' }
];

export default function AntibioticGroupSelect() {
  return (
    <Paper sx={{ padding: '20px' }}>
      <Autocomplete
        disablePortal
        id="antibiotics-group-select"
        options={initialOptions}
        renderInput={(params) => <TextField {...params} label="Grupa antybiotyków" />}
      />
    </Paper>
  );
}

