import * as React from 'react';
import TextField from '@mui/material/TextField';
import Autocomplete from '@mui/material/Autocomplete';
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
    <Autocomplete
    disablePortal
    id="antibiotics-group-select"
    options={initialOptions}
    sx={{ width: '35vh' }}
    renderInput={(params) => <TextField {...params} label="Grupa antybiotyków" />}
  />
  );
}

