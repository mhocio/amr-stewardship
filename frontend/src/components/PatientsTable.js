import * as React from 'react';
import { DataGrid } from '@mui/x-data-grid';
import { Grid } from '@material-ui/core';
import { Typography } from '@mui/material';
import PeopleIcon from '@mui/icons-material/People';

const columns = [
  { field: 'id', headerName: 'ID', width: 60 },
  { field: 'firstName', headerName: 'Imię', width: 120 },
  { field: 'lastName', headerName: 'Nazwisko', width: 120 }
];

const rows = [
  { id: 1, lastName: 'Snow', firstName: 'Jon' },
  { id: 2, lastName: 'Lannister', firstName: 'Cersei' },
  { id: 3, lastName: 'Lannister', firstName: 'Jaime' },
  { id: 4, lastName: 'Stark', firstName: 'Arya' },
  { id: 5, lastName: 'Targaryen', firstName: 'Daenerys' },
  { id: 6, lastName: 'Melisandre', firstName: 'Jarre', },
  { id: 7, lastName: 'Clifford', firstName: 'Ferrara' },
  { id: 8, lastName: 'Frances', firstName: 'Rossini' },
  { id: 9, lastName: 'Roxie', firstName: 'Harvey' },
];

export default function DataTable() {
  return (
    <div style={{ height: '84vh', width: '100%' }}>
      <Grid container direction="row" alignItems="center" spacing={1} wrap="nowrap">
        <Grid item>
          <PeopleIcon fontSize="large" />
        </Grid>
        <Grid item>
          <Typography variant="h5">Pacjenci</Typography>
        </Grid>
      </Grid>
      <DataGrid
        rows={rows}
        columns={columns}
        responsive={'scrollMaxHeight'}
        hideFooter={true}
      />
    </div>
  );
}
